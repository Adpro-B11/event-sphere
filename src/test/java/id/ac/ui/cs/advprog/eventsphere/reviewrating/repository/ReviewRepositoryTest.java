package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.ZonedDateTime;
import java.util.List;

class ReviewRepositoryTest {

    private ReviewRepository reviewRepository;
    private Review testReview;
    private final String TEST_EVENT_ID = "evt_123";
    private final String TEST_USER_ID = "usr_123";

    @BeforeEach
    void setUp() {
        reviewRepository = new InMemoryReviewRepository();

        testReview = new Review();
        testReview.setId("rev_123");
        testReview.setRating(4);
        testReview.setComment("Good event");
        testReview.setCreatedAt(ZonedDateTime.now());
        testReview.setUserId(TEST_USER_ID);
        testReview.setEventId(TEST_EVENT_ID);

        reviewRepository.save(testReview);
    }

    @Test
    void testFindById() {
        Review found = reviewRepository.findById("rev_123");
        assertNotNull(found);
        assertEquals(testReview.getId(), found.getId());
        assertEquals(testReview.getRating(), found.getRating());
    }

    @Test
    void testFindByEventId() {
        // Add another review for the same event
        Review anotherReview = new Review();
        anotherReview.setId("rev_456");
        anotherReview.setRating(5);
        anotherReview.setComment("Excellent event");
        anotherReview.setCreatedAt(ZonedDateTime.now());
        anotherReview.setUserId("usr_456");
        anotherReview.setEventId(TEST_EVENT_ID);
        reviewRepository.save(anotherReview);

        List<Review> eventReviews = reviewRepository.findByEventId(TEST_EVENT_ID);

        assertEquals(2, eventReviews.size());
        assertTrue(eventReviews.stream().anyMatch(r -> r.getId().equals("rev_123")));
        assertTrue(eventReviews.stream().anyMatch(r -> r.getId().equals("rev_456")));
    }

    @Test
    void testFindByUserIdAndEventId() {
        Review found = reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID);

        assertNotNull(found);
        assertEquals(testReview.getId(), found.getId());
    }

    @Test
    void testDelete() {
        reviewRepository.delete("rev_123");
        assertNull(reviewRepository.findById("rev_123"));
    }
}