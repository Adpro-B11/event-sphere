package id.ac.ui.cs.advprog.eventsphere.reviewrating.factory;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewDTOFactory {

    public ReviewDTO createFromReview(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .userId(review.getUserId())
                .username(review.getUsername())
                .eventId(review.getEventId())
                .build();
    }
}