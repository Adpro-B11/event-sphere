package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.UUID;

class ReviewTest {

    @Test
    void testCreateReview() {
        String id = "rev_123";
        int rating = 5;
        String comment = "This event was amazing!";
        ZonedDateTime createdAt = ZonedDateTime.now();
        String userId = "usr_123";
        String eventId = "evt_123";

        Review review = new Review();
        review.setId(id);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(createdAt);
        review.setUserId(userId);
        review.setEventId(eventId);

        assertEquals(id, review.getId());
        assertEquals(rating, review.getRating());
        assertEquals(comment, review.getComment());
        assertEquals(createdAt, review.getCreatedAt());
        assertEquals(userId, review.getUserId());
        assertEquals(eventId, review.getEventId());
        assertNull(review.getUpdatedAt());
    }

    @Test
    void testInvalidRating() {
        Review review = new Review();

        Exception tooLowException = assertThrows(IllegalArgumentException.class, () -> {
            review.setRating(0);
        });

        Exception tooHighException = assertThrows(IllegalArgumentException.class, () -> {
            review.setRating(6);
        });

        assertTrue(tooLowException.getMessage().contains("between 1 and 5"));
        assertTrue(tooHighException.getMessage().contains("between 1 and 5"));
    }

    @Test
    void testUpdateReview() {
        Review review = new Review();
        review.setRating(3);
        review.setComment("Original comment");

        ZonedDateTime beforeUpdate = ZonedDateTime.now();

        review.setRating(5);
        review.setComment("Updated comment");
        review.setUpdatedAt(ZonedDateTime.now());

        assertEquals(5, review.getRating());
        assertEquals("Updated comment", review.getComment());
        assertNotNull(review.getUpdatedAt());
        assertTrue(review.getUpdatedAt().isAfter(beforeUpdate) || review.getUpdatedAt().equals(beforeUpdate));
    }
}