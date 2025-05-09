package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events/{eventId}")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getReviewsByEventId(@PathVariable String eventId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByEventId(eventId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable String eventId,
            @RequestBody CreateReviewRequest request,
            @RequestHeader(name = "X-User-ID", required = false) String userId) {

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        ReviewDTO createdReview = reviewService.createReview(userId, eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestBody UpdateReviewRequest request,
            @RequestHeader(name = "X-User-ID", required = false) String userId) {

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            ReviewDTO updatedReview = reviewService.updateReview(userId, reviewId, request);
            return ResponseEntity.ok(updatedReview);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String eventId,
            @PathVariable String reviewId,
            @RequestHeader(name = "X-User-ID", required = false) String userId) {

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            reviewService.deleteReview(userId, reviewId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}