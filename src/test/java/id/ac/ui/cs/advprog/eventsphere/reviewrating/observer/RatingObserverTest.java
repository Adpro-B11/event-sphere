package id.ac.ui.cs.advprog.eventsphere.reviewrating.observer;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.EventRatingSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RatingObserverTest {

    @MockBean
    private EventRatingSummaryService summaryService;

    @Autowired
    private RatingObserver ratingObserver;

    private Review review;
    private Review oldReviewState;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setId("rev_123_current");
        review.setRating(4);
        review.setComment("Current good event");
        review.setCreatedAt(ZonedDateTime.now());
        review.setUserId("usr_123");
        review.setUsername("testuser");
        review.setEventId("evt_123");

        oldReviewState = new Review();
        oldReviewState.setId(review.getId());
        oldReviewState.setRating(2);
        oldReviewState.setComment("Old comment");
        oldReviewState.setEventId(review.getEventId());
        oldReviewState.setUserId(review.getUserId());
        oldReviewState.setUsername("testuser");
        oldReviewState.setCreatedAt(review.getCreatedAt().minusHours(1));
    }

    @Test
    void testOnReviewCreated_VerifiesInteraction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(summaryService).addReview(anyString(), anyInt());

        ratingObserver.onReviewCreated(review);
        
        // Wait for async execution
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Method should be called within timeout");
        verify(summaryService, times(1)).addReview(review.getEventId(), review.getRating());
    }

    @Test
    void testOnReviewUpdated_VerifiesInteraction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(summaryService).updateReview(anyString(), anyInt(), anyInt());

        ratingObserver.onReviewUpdated(oldReviewState, review);
        
        // Wait for async execution
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Method should be called within timeout");
        verify(summaryService, times(1)).updateReview(
            review.getEventId(), 
            oldReviewState.getRating(), 
            review.getRating()
        );
    }

    @Test
    void testOnReviewDeleted_VerifiesInteraction() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(summaryService).removeReview(anyString(), anyInt());

        ratingObserver.onReviewDeleted(review);
        
        // Wait for async execution
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Method should be called within timeout");
        verify(summaryService, times(1)).removeReview(review.getEventId(), review.getRating());
    }

    @Test
    void testOnReviewCreated_HandlesException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            latch.countDown();
            throw new RuntimeException("Test exception");
        }).when(summaryService).addReview(anyString(), anyInt());

        // Should not throw exception even if summaryService fails
        assertDoesNotThrow(() -> ratingObserver.onReviewCreated(review));
        
        // Wait and verify the method was called
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Method should be called despite exception");
        verify(summaryService, times(1)).addReview(review.getEventId(), review.getRating());
    }

    @Test
    void testOnReviewUpdated_HandlesException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            latch.countDown();
            throw new RuntimeException("Test exception");
        }).when(summaryService).updateReview(anyString(), anyInt(), anyInt());

        // Should not throw exception even if summaryService fails
        assertDoesNotThrow(() -> ratingObserver.onReviewUpdated(oldReviewState, review));
        
        // Wait and verify the method was called
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Method should be called despite exception");
        verify(summaryService, times(1)).updateReview(
            review.getEventId(), 
            oldReviewState.getRating(), 
            review.getRating()
        );
    }

    @Test
    void testOnReviewDeleted_HandlesException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        doAnswer(invocation -> {
            latch.countDown();
            throw new RuntimeException("Test exception");
        }).when(summaryService).removeReview(anyString(), anyInt());

        // Should not throw exception even if summaryService fails
        assertDoesNotThrow(() -> ratingObserver.onReviewDeleted(review));
        
        // Wait and verify the method was called
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Method should be called despite exception");
        verify(summaryService, times(1)).removeReview(review.getEventId(), review.getRating());
    }
}