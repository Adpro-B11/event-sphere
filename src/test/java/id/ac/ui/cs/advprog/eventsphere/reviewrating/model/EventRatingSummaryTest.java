package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventRatingSummaryTest {

    private EventRatingSummary summary;
    private final String EVENT_ID = "evt_123";

    @BeforeEach
    void setUp() {
        summary = new EventRatingSummary(EVENT_ID);
    }

    @Test
    void testInitialState() {
        assertEquals(EVENT_ID, summary.getEventId());
        assertEquals(0, summary.getTotalReviews());
        assertEquals(0.0, summary.getAverageRating(), 0.01);
    }

    @Test
    void testAddReview() {
        summary.addReview(5);
        assertEquals(1, summary.getTotalReviews());
        assertEquals(5.0, summary.getAverageRating(), 0.01);

        summary.addReview(3);
        assertEquals(2, summary.getTotalReviews());
        assertEquals(4.0, summary.getAverageRating(), 0.01);
    }

    @Test
    void testUpdateReview() {
        summary.addReview(3);
        summary.updateReview(3, 5);

        assertEquals(1, summary.getTotalReviews());
        assertEquals(5.0, summary.getAverageRating(), 0.01);
    }

    @Test
    void testRemoveReview() {
        summary.addReview(5);
        summary.addReview(3);
        assertEquals(2, summary.getTotalReviews());
        assertEquals(4.0, summary.getAverageRating(), 0.01);

        summary.removeReview(3);
        assertEquals(1, summary.getTotalReviews());
        assertEquals(5.0, summary.getAverageRating(), 0.01);
    }
}