// src/test/java/id/ac/ui/cs/advprog/eventsphere/reviewrating/model/ReviewTest.java
package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.UUID;

class ReviewTest {

    @Test
    void testReviewCreation() {
        // Arrange
        String id = UUID.randomUUID().toString();
        int rating = 5;
        String comment = "Great event!";
        ZonedDateTime createdAt = ZonedDateTime.now();
        User user = new User();
        Event event = new Event();
        event.setId(UUID.randomUUID().toString());

        // Act
        Review review = new Review();
        review.setId(id);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(createdAt);
        review.setUser(user);
        review.setEvent(event);

        // Assert
        assertEquals(id, review.getId());
        assertEquals(rating, review.getRating());
        assertEquals(comment, review.getComment());
        assertEquals(createdAt, review.getCreatedAt());
        assertNull(review.getUpdatedAt());
        assertEquals(user, review.getUser());
        assertEquals(event, review.getEvent());
    }

    @Test
    void testRatingValidation() {
        // Arrange
        Review review = new Review();

        // Act & Assert - Valid ratings
        assertDoesNotThrow(() -> review.setRating(1));
        assertDoesNotThrow(() -> review.setRating(3));
        assertDoesNotThrow(() -> review.setRating(5));

        // Act & Assert - Invalid ratings
        IllegalArgumentException exceptionLow = assertThrows(IllegalArgumentException.class,
                () -> review.setRating(0));
        assertEquals("Rating must be between 1 and 5", exceptionLow.getMessage());

        IllegalArgumentException exceptionHigh = assertThrows(IllegalArgumentException.class,
                () -> review.setRating(6));
        assertEquals("Rating must be between 1 and 5", exceptionHigh.getMessage());
    }
}