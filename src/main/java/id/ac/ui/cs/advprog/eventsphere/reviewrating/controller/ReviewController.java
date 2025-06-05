package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.EventRatingSummaryDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.EventRatingSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/events/{eventId}")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final EventRatingSummaryService ratingSummaryService;
    private final Executor taskExecutor;

    @GetMapping("/reviews")
    public CompletableFuture<ResponseEntity<List<ReviewDTO>>> getReviewsByEventId(@PathVariable String eventId) {
        return CompletableFuture.supplyAsync(() -> {
            
            try {
                List<ReviewDTO> reviews = reviewService.getReviewsByEventId(eventId);
                return ResponseEntity.ok(reviews);
            } catch (IllegalStateException ex) {
                return ResponseEntity.badRequest().build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PostMapping("/reviews")
    public CompletableFuture<ResponseEntity<ReviewDTO>> createReview(
            @PathVariable String eventId,
            @RequestBody ReviewRequest request) {
                
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Set eventId from path parameter
                request.setEventId(eventId);
                ReviewDTO createdReview = reviewService.createReview(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);

            } catch (IllegalStateException ex) {
                if (ex.getMessage().contains("already reviewed")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } catch (UnauthorizedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }, taskExecutor);
    }

    @PutMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<ReviewDTO>> updateReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestBody ReviewRequest request) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                // Set eventId from path parameter
                request.setEventId(eventId);
                
                ReviewDTO updatedReview = reviewService.updateReview(reviewId, request);
                return ResponseEntity.ok(updatedReview);

            } catch (NotFoundException ex) {
                return ResponseEntity.notFound().build();
            } catch (UnauthorizedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (IllegalStateException ex) {
                return ResponseEntity.badRequest().build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }, taskExecutor);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<Void>> deleteReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestBody ReviewRequest request) {

        return CompletableFuture.supplyAsync(() -> {
            
            try {
                request.setEventId(eventId);
                
                reviewService.deleteReview(reviewId, request);
                return ResponseEntity.noContent().<Void>build();

            } catch (NotFoundException ex) {
                return ResponseEntity.notFound().build();
            } catch (UnauthorizedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (IllegalStateException ex) {
                return ResponseEntity.badRequest().build();
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
            }
        }, taskExecutor);
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
