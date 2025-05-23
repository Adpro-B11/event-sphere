package id.ac.ui.cs.advprog.eventsphere.reviewrating.dto;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewDTOFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;

class ReviewDTOFactoryTest {

    @Test
    void testCreateReviewDTO() {
        // Set up a review
        Review review = new Review();
        review.setId("rev_123");
        review.setRating(5);
        review.setComment("Great event!");
        review.setCreatedAt(ZonedDateTime.now());
        review.setUserId("usr_123");
        review.setEventId("evt_123");

        // Create user data
        String username = "John Doe";

        // Create DTO with factory
        ReviewDTOFactory factory = new ReviewDTOFactory();
        ReviewDTO dto = factory.createFromReview(review, username);

        // Verify DTO contents
        assertEquals(review.getId(), dto.getId());
        assertEquals(review.getRating(), dto.getRating());
        assertEquals(review.getComment(), dto.getComment());
        assertEquals(review.getCreatedAt(), dto.getCreatedAt());
        assertEquals(review.getUserId(), dto.getUserId());
        assertEquals(username, dto.getUsername());
    }

    @Test
    void testCreateReviewDTOWithUpdatedTime() {
        // Set up a review with updated time
        Review review = new Review();
        review.setId("rev_123");
        review.setRating(4);
        review.setComment("Updated comment");
        review.setCreatedAt(ZonedDateTime.now().minusDays(1));
        review.setUpdatedAt(ZonedDateTime.now());
        review.setUserId("usr_123");
        review.setEventId("evt_123");

        // Create DTO with factory
        ReviewDTOFactory factory = new ReviewDTOFactory();
        ReviewDTO dto = factory.createFromReview(review, "Jane Smith");

        // Verify DTO includes updated time
        assertNotNull(dto.getUpdatedAt());
        assertEquals(review.getUpdatedAt(), dto.getUpdatedAt());
    }
}