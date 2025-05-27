package id.ac.ui.cs.advprog.eventsphere.reviewrating.factory;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewFactory {

    public Review createFromRequest(ReviewRequest request) {
        return Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .userId(request.getUserId())
                .username(request.getUsername())
                .eventId(request.getEventId())
                .build();
    }

    public Review createCopy(Review original) {
        return Review.builder()
                .id(original.getId())
                .rating(original.getRating())
                .comment(original.getComment())
                .createdAt(original.getCreatedAt())
                .updatedAt(original.getUpdatedAt())
                .userId(original.getUserId())
                .username(original.getUsername())
                .eventId(original.getEventId())
                .build();
    }

    public void updateFromRequest(Review review, ReviewRequest request) {
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        // Only update username if provided
        if (request.getUsername() != null) {
            review.setUsername(request.getUsername());
        }
    }
}