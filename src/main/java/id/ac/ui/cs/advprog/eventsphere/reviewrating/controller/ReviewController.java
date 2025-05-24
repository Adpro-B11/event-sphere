package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.EventRatingSummaryDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.EventRatingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/events/{eventId}")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final EventRatingSummaryService ratingSummaryService;

    @GetMapping("/reviews")
    public CompletableFuture<ResponseEntity<List<ReviewDTO>>> getReviewsByEventId(@PathVariable String eventId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<ReviewDTO> reviews = reviewService.getReviewsByEventId(eventId);
                return ResponseEntity.ok(reviews);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PostMapping("/reviews")
    public CompletableFuture<ResponseEntity<ReviewDTO>> createReview(
            @PathVariable String eventId,
            @RequestBody CreateReviewRequest request,
            @RequestHeader(name = "X-User-ID", required = true) String userId) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                ReviewDTO createdReview = reviewService.createReview(userId, eventId, request);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
            } catch (IllegalStateException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PutMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<ReviewDTO>> updateReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestBody UpdateReviewRequest request,
            @RequestHeader(name = "X-User-ID", required = true) String userId) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                ReviewDTO updatedReview = reviewService.updateReview(userId, reviewId, request);
                return ResponseEntity.ok(updatedReview);
            } catch (NotFoundException ex) {
                return ResponseEntity.notFound().build();
            } catch (UnauthorizedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @DeleteMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<Void>> deleteReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestHeader(name = "X-User-ID", required = true) String userId) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                reviewService.deleteReview(userId, reviewId);
                return ResponseEntity.noContent().<Void>build();
            } catch (NotFoundException ex) {
                return ResponseEntity.notFound().build();
            } catch (UnauthorizedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
            }
        });
    }

    @GetMapping("/rating-summary")
    public CompletableFuture<ResponseEntity<EventRatingSummaryDTO>> getEventRatingSummary(
            @PathVariable String eventId) {
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                double averageRating = ratingSummaryService.getAverageRating(eventId);
                int totalReviews = ratingSummaryService.getTotalReviews(eventId);
                
                EventRatingSummaryDTO response = EventRatingSummaryDTO.builder()
                        .eventId(eventId)
                        .averageRating(averageRating)
                        .totalReviews(totalReviews)
                        .build();
                        
                return ResponseEntity.ok(response);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }
}