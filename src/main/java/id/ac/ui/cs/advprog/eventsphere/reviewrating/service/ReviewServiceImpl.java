package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewDTOFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.ReviewRepository;
import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.observer.RatingSubject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RatingSubject ratingSubject;
    private final ReviewDTOFactory dtoFactory;
    private final ReviewFactory reviewFactory;
    private final TicketService ticketService;
    private final EventService eventService;

    @Override
    public ReviewDTO createReview(ReviewRequest request) {
        log.info("Creating review for event: {} by user: {}", request.getEventId(), request.getUserId());
        
        validateEventFinished(request.getEventId());
        validateUserHasTicket(request.getUserId(), request.getEventId());
        validateUserHasNotReviewed(request.getUserId(), request.getEventId());

        // Create review using factory
        Review review = reviewFactory.createFromRequest(request);
        Review savedReview = reviewRepository.save(review);
        
        log.info("Review created successfully. ID: {}, Event: {}, User: {}, Rating: {}", 
                savedReview.getId(), request.getEventId(), request.getUserId(), request.getRating());

        // Notify observers about the new review
        ratingSubject.notifyReviewCreated(savedReview);
        log.debug("Rating observers notified for new review: {}", savedReview.getId());

        return dtoFactory.createFromReview(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByEventId(String eventId) {
        log.info("Fetching reviews for event: {}", eventId);
        
        validateEventFinished(eventId);

        List<Review> reviews = reviewRepository.findByEventId(eventId);
        log.info("Found {} reviews for event: {}", reviews.size(), eventId);

        return reviews.stream()
                .map(dtoFactory::createFromReview)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(String reviewId, ReviewRequest request) {
        log.info("Updating review: {} by user: {}", reviewId, request.getUserId());
        
        validateEventFinished(request.getEventId());
        validateUserHasTicket(request.getUserId(), request.getEventId());

        Review review = findReviewById(reviewId);
        validateReviewOwnership(review, request.getUserId());

        // Create copy for observer notification using factory
        Review oldReview = reviewFactory.createCopy(review);
        int oldRating = review.getRating();

        // Update review using factory
        reviewFactory.updateFromRequest(review, request);
        Review updatedReview = reviewRepository.save(review);
        
        log.info("Review updated successfully. ID: {}, OldRating: {}, NewRating: {}", 
                reviewId, oldRating, request.getRating());

        // Notify observers about the update
        ratingSubject.notifyReviewUpdated(oldReview, updatedReview);
        log.debug("Rating observers notified for updated review: {}", reviewId);

        return dtoFactory.createFromReview(updatedReview);
    }

    @Override
    public void deleteReview(String reviewId, ReviewRequest request) {
        log.info("Deleting review: {} by user: {}", reviewId, request.getUserId());
        
        Review review = findReviewById(reviewId);
        validateEventFinished(review.getEventId());
        validateUserHasTicket(request.getUserId(), review.getEventId());
        validateReviewOwnership(review, request.getUserId());

        // Create copy for observer notification using factory
        Review reviewCopy = reviewFactory.createCopy(review);

        reviewRepository.delete(review);
        log.info("Review deleted successfully. ID: {}, Event: {}, User: {}", 
                reviewId, review.getEventId(), review.getUserId());

        // Notify observers about the deletion
        ratingSubject.notifyReviewDeleted(reviewCopy);
        log.debug("Rating observers notified for deleted review: {}", reviewId);
    }

    // Private validation methods to reduce code duplication
    private void validateEventFinished(String eventId) {
        if (!eventService.isEventFinished(eventId)) {
            log.warn("Operation failed - event not finished: {}", eventId);
            throw new IllegalStateException("Reviews can only be processed for finished events");
        }
    }

    private void validateUserHasTicket(String userId, String eventId) {
        if (!ticketService.userHasTicket(userId, eventId)) {
            log.warn("Operation failed - user has no ticket. User: {}, Event: {}", userId, eventId);
            throw new UnauthorizedException("Only users with tickets can access reviews for this event");
        }
    }

    private void validateUserHasNotReviewed(String userId, String eventId) {
        Optional<Review> existingReview = reviewRepository.findByUserIdAndEventId(userId, eventId);
        if (existingReview.isPresent()) {
            log.warn("Review creation failed - user already reviewed event. User: {}, Event: {}", userId, eventId);
            throw new IllegalStateException("User has already reviewed this event");
        }
    }

    private Review findReviewById(String reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    log.error("Review not found: {}", reviewId);
                    return new NotFoundException("Review not found with id: " + reviewId);
                });
    }

    private void validateReviewOwnership(Review review, String userId) {
        if (!review.getUserId().equals(userId)) {
            log.warn("Operation failed - unauthorized user. ReviewId: {}, UserId: {}, ReviewOwner: {}", 
                    review.getId(), userId, review.getUserId());
            throw new UnauthorizedException("User is not authorized to access this review");
        }
    }
}