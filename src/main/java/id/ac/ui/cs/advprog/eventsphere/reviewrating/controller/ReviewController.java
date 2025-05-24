package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/events/{eventId}")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Qualifier("taskExecutor") 
    private final Executor taskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @GetMapping("/reviews")
    public CompletableFuture<ResponseEntity<List<ReviewDTO>>> getReviewsByEventId(@PathVariable String eventId) {
        return CompletableFuture.supplyAsync(() -> {
            List<ReviewDTO> reviews = reviewService.getReviewsByEventId(eventId);
            return ResponseEntity.ok(reviews);
        }, taskExecutor).exceptionally(ex -> {
            logger.error("Error fetching reviews for eventId {}: {}", eventId, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        });
    }

    @PostMapping("/reviews")
    public CompletableFuture<ResponseEntity<ReviewDTO>> createReview(
            @PathVariable String eventId,
            @RequestBody CreateReviewRequest request,
            @RequestHeader(name = "X-User-ID", required = true) String userId) { 

        return CompletableFuture.supplyAsync(() -> {
            ReviewDTO createdReview = reviewService.createReview(userId, eventId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        }, taskExecutor).exceptionally(ex -> {
            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
            if (cause instanceof IllegalStateException) { 
                logger.warn("Conflict creating review for eventId {} by userId {}: {}", eventId, userId, cause.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            logger.error("Error creating review for eventId {}: {}", eventId, cause.getMessage(), cause);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        });
    }

    @PutMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<ReviewDTO>> updateReview(
            @PathVariable String eventId, 
            @PathVariable String reviewId,
            @RequestBody UpdateReviewRequest request,
            @RequestHeader(name = "X-User-ID", required = true) String userId) {

        return CompletableFuture.supplyAsync(() -> {
            ReviewDTO updatedReview = reviewService.updateReview(userId, reviewId, request);
            return ResponseEntity.ok(updatedReview);
        }, taskExecutor).exceptionally(ex -> {
            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
            if (cause instanceof NotFoundException) {
                logger.warn("Review not found for update: {}", reviewId);
                return ResponseEntity.notFound().build();
            } else if (cause instanceof UnauthorizedException) {
                logger.warn("Unauthorized update attempt for review {} by user {}", reviewId, userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            logger.error("Error updating review {}: {}", reviewId, cause.getMessage(), cause);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        });
    }

    @DeleteMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<Void>> deleteReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestHeader(name = "X-User-ID", required = true) String userId) {

        return CompletableFuture.runAsync(() -> {
            reviewService.deleteReview(userId, reviewId);
        }, taskExecutor)
        .thenApply(voidResult -> ResponseEntity.noContent().<Void>build())
        .exceptionally(ex -> {
            Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
            if (cause instanceof NotFoundException) {
                logger.warn("Review not found for delete: {}", reviewId);
                return ResponseEntity.notFound().build();
            } else if (cause instanceof UnauthorizedException) {
                logger.warn("Unauthorized delete attempt for review {} by user {}", reviewId, userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            logger.error("Error deleting review {}: {}", reviewId, cause.getMessage(), cause);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
        });
    }
}