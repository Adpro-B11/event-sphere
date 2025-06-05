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
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
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

        String userId = getCurrentUserId();
        
        validateEventFinished(request.getEventId());
        validateUserHasTicket(userId, request.getEventId());
        validateUserHasNotReviewed(userId, request.getEventId());

        // Create review using factory
        request.setUserId(userId);
        Review review = reviewFactory.createFromRequest(request);
        Review savedReview = reviewRepository.save(review);

        // Notify observers about the new review
        ratingSubject.notifyReviewCreated(savedReview);

        return dtoFactory.createFromReview(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByEventId(String eventId) {
        
        validateEventFinished(eventId);

        List<Review> reviews = reviewRepository.findByEventId(eventId);

        return reviews.stream()
                .map(dtoFactory::createFromReview)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewDTO updateReview(String reviewId, ReviewRequest request) {
        
        String userId = getCurrentUserId();
        System.out.println("current user"+userId);

        validateEventFinished(request.getEventId());
        validateUserHasTicket(userId, request.getEventId());

        Review review = findReviewById(reviewId);
        validateReviewOwnership(review, userId);

        // Create copy for observer notification using factory
        Review oldReview = reviewFactory.createCopy(review);

        // Update review using factory
        request.setUserId(userId);
        reviewFactory.updateFromRequest(review, request);
        Review updatedReview = reviewRepository.save(review);
        

        // Notify observers about the update
        ratingSubject.notifyReviewUpdated(oldReview, updatedReview);

        return dtoFactory.createFromReview(updatedReview);
    }

    @Override
    public void deleteReview(String reviewId, ReviewRequest request) {
        
        String userId = getCurrentUserId();
        System.out.println("current user"+userId);

        Review review = findReviewById(reviewId);
        validateEventFinished(review.getEventId());
        validateUserHasTicket(userId, review.getEventId());
        validateReviewOwnership(review, userId);

        // Create copy for observer notification using factory
        Review reviewCopy = reviewFactory.createCopy(review);

        reviewRepository.delete(review);

        // Notify observers about the deletion
        ratingSubject.notifyReviewDeleted(reviewCopy);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("current authentication"+authentication);
        System.out.println("current authentication principal"+authentication.getPrincipal());
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal().toString())) {
            throw new SecurityException("No authenticated user found");
        }
        return authentication.getName();
    }

    // Private validation methods to reduce code duplication
    private void validateEventFinished(String eventId) {
        if (!eventService.isEventFinished(eventId)) {
            throw new IllegalStateException("Reviews can only be processed for finished events");
        }
    }

    private void validateUserHasTicket(String userId, String eventId) {
        if (!ticketService.userHasTicket(userId, eventId)) {
            throw new UnauthorizedException("Only users with tickets can access reviews for this event");
        }
    }

    private void validateUserHasNotReviewed(String userId, String eventId) {
        Optional<Review> existingReview = reviewRepository.findByUserIdAndEventId(userId, eventId);
        if (existingReview.isPresent()) {
            throw new IllegalStateException("User has already reviewed this event");
        }
    }

    private Review findReviewById(String reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> {
                    return new NotFoundException("Review not found with id: " + reviewId);
                });
    }

    private void validateReviewOwnership(Review review, String userId) {
        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException("User is not authorized to access this review");
        }
    }
}