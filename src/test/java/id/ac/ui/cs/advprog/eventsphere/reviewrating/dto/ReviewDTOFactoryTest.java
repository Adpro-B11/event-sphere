package id.ac.ui.cs.advprog.eventsphere.reviewrating.factory;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
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
        review.setUsername("John Doe");
        review.setEventId("evt_123");

        // Create DTO with factory
        ReviewDTOFactory factory = new ReviewDTOFactory();
        ReviewDTO dto = factory.createFromReview(review);

        // Verify DTO contents
        assertEquals(review.getId(), dto.getId());
        assertEquals(review.getRating(), dto.getRating());
        assertEquals(review.getComment(), dto.getComment());
        assertEquals(review.getCreatedAt(), dto.getCreatedAt());
        assertEquals(review.getUserId(), dto.getUserId());
        assertEquals(review.getUsername(), dto.getUsername());
        assertEquals(review.getEventId(), dto.getEventId());
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
        review.setUsername("Jane Smith");
        review.setEventId("evt_123");

        // Create DTO with factory
        ReviewDTOFactory factory = new ReviewDTOFactory();
        ReviewDTO dto = factory.createFromReview(review);

        // Verify DTO includes updated time
        assertNotNull(dto.getUpdatedAt());
        assertEquals(review.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(review.getUsername(), dto.getUsername());
    }
}