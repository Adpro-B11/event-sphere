package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.auth.service.UserService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewDTOFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RatingSubject ratingSubject;
    private final ReviewDTOFactory dtoFactory;
    private final UserService userService;

    @Override
    public ReviewDTO createReview(String userId, String eventId, CreateReviewRequest request) {
        // Check if the user has already reviewed this event
        Optional<Review> existingReviewOptional = reviewRepository.findByUserIdAndEventId(userId, eventId);
        if (existingReviewOptional.isPresent()) {
            throw new IllegalStateException("User has already reviewed this event");
        }

        // Create a new review
        Review review = new Review();
        review.setId("rev_" + UUID.randomUUID().toString().substring(0, 12));
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUserId(userId);
        review.setEventId(eventId);

        // Save the review
        Review savedReview = reviewRepository.save(review);

        // Notify observers about the new review
        ratingSubject.notifyReviewCreated(savedReview);

        // Create and return the DTO
        String username = userService.getUsernameById(userId);
        return dtoFactory.createFromReview(savedReview, username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByEventId(String eventId) {
        // Get all reviews for the event
        List<Review> reviews = reviewRepository.findByEventId(eventId);

        // Convert to DTOs
        return reviews.stream()
                .map(review -> {
                    String username = userService.getUsernameById(review.getUserId());
                    return dtoFactory.createFromReview(review, username);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(String userId, String reviewId, UpdateReviewRequest request) {
        // Find the review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        // Check if the user is the author of the review
        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to update this review");
        }

        // Save the old review state for observer notification (manual deep copy for relevant fields)
        Review oldReview = new Review();
        oldReview.setId(review.getId());
        oldReview.setRating(review.getRating()); // Capture old rating
        oldReview.setComment(review.getComment());
        oldReview.setCreatedAt(review.getCreatedAt());
        oldReview.setUserId(review.getUserId());
        oldReview.setEventId(review.getEventId());
        // oldReview.setUpdatedAt(review.getUpdatedAt()); // Not needed for old state comparison here

        // Update the review
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        // review.setUpdatedAt(ZonedDateTime.now()); // Handled by @PreUpdate

        // Save the updated review
        Review updatedReview = reviewRepository.save(review);

        // Notify observers about the update
        ratingSubject.notifyReviewUpdated(oldReview, updatedReview);

        // Create and return the DTO
        String username = userService.getUsernameById(userId);
        return dtoFactory.createFromReview(updatedReview, username);
    }

    @Override
    public void deleteReview(String userId, String reviewId) {
        // Find the review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));


        // Check if the user is the author of the review
        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to delete this review");
        }

        // Delete the review
        reviewRepository.delete(review); // Use delete(entity) or deleteById(id)

        // Notify observers about the deletion
        ratingSubject.notifyReviewDeleted(review);
    }
}