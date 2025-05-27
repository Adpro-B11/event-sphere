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

@RestController
@RequestMapping("/api/events/{eventId}")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final EventRatingSummaryService ratingSummaryService;

    @GetMapping("/reviews")
    public CompletableFuture<ResponseEntity<List<ReviewDTO>>> getReviewsByEventId(@PathVariable String eventId) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("GET /api/events/{}/reviews - Fetching reviews", eventId);
            
            try {
                List<ReviewDTO> reviews = reviewService.getReviewsByEventId(eventId);
                log.info("Successfully retrieved {} reviews for event: {}", reviews.size(), eventId);
                return ResponseEntity.ok(reviews);
            } catch (IllegalStateException ex) {
                log.warn("Failed to get reviews for event {}: {}", eventId, ex.getMessage());
                return ResponseEntity.badRequest().build();
            } catch (Exception ex) {
                log.error("Unexpected error getting reviews for event {}: {}", eventId, ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PostMapping("/reviews")
    public CompletableFuture<ResponseEntity<ReviewDTO>> createReview(
            @PathVariable String eventId,
            @RequestBody ReviewRequest request) {
                
        return CompletableFuture.supplyAsync(() -> {
            log.info("POST /api/events/{}/reviews - Creating review by user: {}", eventId, request.getUserId());
            
            try {
                // Set eventId from path parameter
                request.setEventId(eventId);
                
                ReviewDTO createdReview = reviewService.createReview(request);
                log.info("Successfully created review {} for event: {} by user: {}", 
                        createdReview.getId(), eventId, request.getUserId());
                return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);

            } catch (IllegalStateException ex) {
                if (ex.getMessage().contains("already reviewed")) {
                    log.warn("Review creation failed - user already reviewed: User: {}, Event: {}", 
                            request.getUserId(), eventId);
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                } else {
                    log.warn("Review creation failed - event not finished: Event: {}", eventId);
                    return ResponseEntity.badRequest().build();
                }
            } catch (UnauthorizedException ex) {
                log.warn("Review creation failed - unauthorized: User: {}, Event: {}, Reason: {}", 
                        request.getUserId(), eventId, ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (Exception ex) {
                log.error("Unexpected error creating review for event {}: {}", eventId, ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @PutMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<ReviewDTO>> updateReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestBody ReviewRequest request) {

        return CompletableFuture.supplyAsync(() -> {
            log.info("PUT /api/events/{}/reviews/{} - Updating review by user: {}", 
                    eventId, reviewId, request.getUserId());
            
            try {
                // Set eventId from path parameter
                request.setEventId(eventId);
                
                ReviewDTO updatedReview = reviewService.updateReview(reviewId, request);
                log.info("Successfully updated review {} for event: {} by user: {}", 
                        reviewId, eventId, request.getUserId());
                return ResponseEntity.ok(updatedReview);

            } catch (NotFoundException ex) {
                log.warn("Review update failed - review not found: {}", reviewId);
                return ResponseEntity.notFound().build();
            } catch (UnauthorizedException ex) {
                log.warn("Review update failed - unauthorized: User: {}, Review: {}, Reason: {}", 
                        request.getUserId(), reviewId, ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (IllegalStateException ex) {
                log.warn("Review update failed - invalid state: Review: {}, Reason: {}", 
                        reviewId, ex.getMessage());
                return ResponseEntity.badRequest().build();
            } catch (Exception ex) {
                log.error("Unexpected error updating review {}: {}", reviewId, ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }

    @DeleteMapping("/reviews/{reviewId}")
    public CompletableFuture<ResponseEntity<Void>> deleteReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestBody ReviewRequest request) {

        return CompletableFuture.supplyAsync(() -> {
            log.info("DELETE /api/events/{}/reviews/{} - Deleting review by user: {}", 
                    eventId, reviewId, request.getUserId());
            
            try {
                request.setEventId(eventId);
                
                reviewService.deleteReview(reviewId, request);
                log.info("Successfully deleted review {} for event: {} by user: {}", 
                        reviewId, eventId, request.getUserId());
                return ResponseEntity.noContent().<Void>build();

            } catch (NotFoundException ex) {
                log.warn("Review deletion failed - review not found: {}", reviewId);
                return ResponseEntity.notFound().build();
            } catch (UnauthorizedException ex) {
                log.warn("Review deletion failed - unauthorized: User: {}, Review: {}, Reason: {}", 
                        request.getUserId(), reviewId, ex.getMessage());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } catch (IllegalStateException ex) {
                log.warn("Review deletion failed - invalid state: Review: {}, Reason: {}", 
                        reviewId, ex.getMessage());
                return ResponseEntity.badRequest().build();
            } catch (Exception ex) {
                log.error("Unexpected error deleting review {}: {}", reviewId, ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
            }
        });
    }

    @GetMapping("/rating-summary")
    public CompletableFuture<ResponseEntity<EventRatingSummaryDTO>> getEventRatingSummary(
            @PathVariable String eventId) {
        
        return CompletableFuture.supplyAsync(() -> {
            log.info("GET /api/events/{}/rating-summary - Fetching rating summary", eventId);
            
            try {
                double averageRating = ratingSummaryService.getAverageRating(eventId);
                int totalReviews = ratingSummaryService.getTotalReviews(eventId);
                
                EventRatingSummaryDTO response = EventRatingSummaryDTO.builder()
                        .eventId(eventId)
                        .averageRating(averageRating)
                        .totalReviews(totalReviews)
                        .build();
                
                log.info("Successfully retrieved rating summary for event {}: avg={:.2f}, total={}", 
                        eventId, averageRating, totalReviews);
                return ResponseEntity.ok(response);
            } catch (Exception ex) {
                log.error("Unexpected error getting rating summary for event {}: {}", 
                        eventId, ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }
}