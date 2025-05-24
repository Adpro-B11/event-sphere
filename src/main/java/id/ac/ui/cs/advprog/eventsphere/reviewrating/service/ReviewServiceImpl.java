package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewDTOFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.ReviewRepository;
import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;
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
    private final TicketService ticketService;
    private final EventService eventService;

    @Override
    public ReviewDTO createReview(ReviewRequest request) {
        // Check if event is finished
        if (!eventService.isEventFinished(request.getEventId())) {
            throw new IllegalStateException("Reviews can only be created for finished events");
        }

        // Check if user has ticket for this event
        if (!ticketService.userHasTicket(request.getUserId(), request.getEventId())) {
            throw new UnauthorizedException("Only users with tickets can review this event");
        }

        // Check if the user has already reviewed this event
        Optional<Review> existingReviewOptional = reviewRepository.findByUserIdAndEventId(
                request.getUserId(), request.getEventId());
        if (existingReviewOptional.isPresent()) {
            throw new IllegalStateException("User has already reviewed this event");
        }

        // Create a new review
        Review review = new Review();
        review.setId("rev_" + UUID.randomUUID().toString().substring(0, 12));
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUserId(request.getUserId());
        review.setUsername(request.getUsername());
        review.setEventId(request.getEventId());

        // Save the review
        Review savedReview = reviewRepository.save(review);

        // Notify observers about the new review
        ratingSubject.notifyReviewCreated(savedReview);

        // Create and return the DTO
        return dtoFactory.createFromReview(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByEventId(String eventId) {
        // Check if event is finished - anyone can view reviews for finished events
        if (!eventService.isEventFinished(eventId)) {
            throw new IllegalStateException("Reviews can only be viewed for finished events");
        }

        // Get all reviews for the event
        List<Review> reviews = reviewRepository.findByEventId(eventId);

        // Convert to DTOs
        return reviews.stream()
                .map(dtoFactory::createFromReview)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(String reviewId, ReviewRequest request) {
        // Check if event is finished
        if (!eventService.isEventFinished(request.getEventId())) {
            throw new IllegalStateException("Reviews can only be updated for finished events");
        }

        // Check if user has ticket for this event
        if (!ticketService.userHasTicket(request.getUserId(), request.getEventId())) {
            throw new UnauthorizedException("Only users with tickets can update reviews for this event");
        }

        // Find the review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        // Check if the user is the author of the review
        if (!review.getUserId().equals(request.getUserId())) {
            throw new UnauthorizedException("User is not authorized to update this review");
        }

        // Save the old review state for observer notification
        Review oldReview = new Review();
        oldReview.setId(review.getId());
        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());
        oldReview.setCreatedAt(review.getCreatedAt());
        oldReview.setUserId(review.getUserId());
        oldReview.setUsername(review.getUsername());
        oldReview.setEventId(review.getEventId());

        // Update the review
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        // Username might be updated too
        if (request.getUsername() != null) {
            review.setUsername(request.getUsername());
        }

        // Save the updated review
        Review updatedReview = reviewRepository.save(review);

        // Notify observers about the update
        ratingSubject.notifyReviewUpdated(oldReview, updatedReview);

        // Create and return the DTO
        return dtoFactory.createFromReview(updatedReview);
    }

    @Override
    public void deleteReview(String reviewId, ReviewRequest request) {
        // Find the review first to get the eventId
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        // Check if event is finished
        if (!eventService.isEventFinished(review.getEventId())) {
            throw new IllegalStateException("Reviews can only be deleted for finished events");
        }

        // Check if user has ticket for this event
        if (!ticketService.userHasTicket(request.getUserId(), review.getEventId())) {
            throw new UnauthorizedException("Only users with tickets can delete reviews for this event");
        }

        // Check if the user is the author of the review
        if (!review.getUserId().equals(request.getUserId())) {
            throw new UnauthorizedException("User is not authorized to delete this review");
        }

        // Delete the review
        reviewRepository.delete(review);

        // Notify observers about the deletion
        ratingSubject.notifyReviewDeleted(review);
    }
}