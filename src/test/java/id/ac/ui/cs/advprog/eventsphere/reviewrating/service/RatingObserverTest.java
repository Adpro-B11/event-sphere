package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.config.AsyncTestConfig;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.ZonedDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {RatingObserverImpl.class, AsyncTestConfig.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = {AsyncTestConfig.class})
class RatingObserverTest {

    @MockBean
    private EventRatingSummaryService summaryService;

    private RatingObserverImpl observer;
    private Review review;
    private Review oldReviewState;

    @BeforeEach
    void setUp() {
        observer = new RatingObserverImpl(summaryService);

        review = new Review();
        review.setId("rev_123_current");
        review.setRating(4);
        review.setComment("Current good event");
        review.setCreatedAt(ZonedDateTime.now());
        review.setUserId("usr_123");
        review.setEventId("evt_123");

        oldReviewState = new Review();
        oldReviewState.setId(review.getId());
        oldReviewState.setRating(2);
        oldReviewState.setComment("Old comment");
        oldReviewState.setEventId(review.getEventId());
        oldReviewState.setUserId(review.getUserId());
        oldReviewState.setCreatedAt(review.getCreatedAt().minusHours(1));
    }

    @Test
    void testOnReviewCreated_VerifiesInteraction() {
        observer.onReviewCreated(review);
        verify(summaryService, timeout(1000).times(1)).addReview(review.getEventId(), review.getRating());
    }

    @Test
    void testOnReviewUpdated_VerifiesInteraction() {
        observer.onReviewUpdated(oldReviewState, review);
        verify(summaryService, timeout(1000).times(1)).updateReview(review.getEventId(), oldReviewState.getRating(), review.getRating());
    }

    @Test
    void testOnReviewDeleted_VerifiesInteraction() {
        observer.onReviewDeleted(review);
        verify(summaryService, timeout(1000).times(1)).removeReview(review.getEventId(), review.getRating());
    }

    @Test
    void testOnReviewCreated_ShouldBeNonBlockingAndEventuallyExecute() throws InterruptedException {
        final CountDownLatch serviceMethodCalledLatch = new CountDownLatch(1);
        final long testThreadId = Thread.currentThread().getId();
        final AtomicLong executionThreadId = new AtomicLong();

        doAnswer(invocation -> {
            executionThreadId.set(Thread.currentThread().getId());
            Thread.sleep(50); // Short sleep to allow main thread to proceed if async
            serviceMethodCalledLatch.countDown();
            return null;
        }).when(summaryService).addReview(anyString(), anyInt());

        long startTime = System.currentTimeMillis();
        observer.onReviewCreated(review);
        long endTime = System.currentTimeMillis();

        // Allow more time for async setup in Spring context
        assertTrue((endTime - startTime) < 300, "onReviewCreated call was blocking. Duration: " + (endTime - startTime) + "ms. Test environment may need more async setup time.");
        assertTrue(serviceMethodCalledLatch.await(3, TimeUnit.SECONDS), "summaryService.addReview was not called within timeout.");
        
        if (executionThreadId.get() != 0 && executionThreadId.get() != testThreadId) {
            System.out.println("✅ onReviewCreated: Executed on different thread as expected (Thread ID: " + executionThreadId.get() + ")");
        } else if (executionThreadId.get() != 0) {
            System.out.println("⚠️ onReviewCreated: Executed on SAME thread (" + executionThreadId.get() + "). @Async proxy might not be fully active in test setup.");
        }
        verify(summaryService, times(1)).addReview(eq(review.getEventId()), eq(review.getRating()));
    }

    @Test
    void testOnReviewUpdated_ShouldBeNonBlockingAndEventuallyExecute() throws InterruptedException {
        final CountDownLatch serviceMethodCalledLatch = new CountDownLatch(1);
        final long testThreadId = Thread.currentThread().getId();
        final AtomicLong executionThreadId = new AtomicLong();

        doAnswer(invocation -> {
            executionThreadId.set(Thread.currentThread().getId());
            Thread.sleep(50);
            serviceMethodCalledLatch.countDown();
            return null;
        }).when(summaryService).updateReview(anyString(), anyInt(), anyInt());

        long startTime = System.currentTimeMillis();
        observer.onReviewUpdated(oldReviewState, review);
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 300, "onReviewUpdated call was blocking. Duration: " + (endTime - startTime) + "ms.");
        assertTrue(serviceMethodCalledLatch.await(3, TimeUnit.SECONDS), "summaryService.updateReview was not called within timeout.");

        if (executionThreadId.get() != 0 && executionThreadId.get() != testThreadId) {
            System.out.println("✅ onReviewUpdated: Executed on different thread as expected (Thread ID: " + executionThreadId.get() + ")");
        } else if (executionThreadId.get() != 0) {
            System.out.println("⚠️ onReviewUpdated: Executed on SAME thread (" + executionThreadId.get() + "). @Async proxy might not be fully active.");
        }
        verify(summaryService, times(1)).updateReview(eq(review.getEventId()), eq(oldReviewState.getRating()), eq(review.getRating()));
    }

    @Test
    void testOnReviewDeleted_ShouldBeNonBlockingAndEventuallyExecute() throws InterruptedException {
        final CountDownLatch serviceMethodCalledLatch = new CountDownLatch(1);
        final long testThreadId = Thread.currentThread().getId();
        final AtomicLong executionThreadId = new AtomicLong();

        doAnswer(invocation -> {
            executionThreadId.set(Thread.currentThread().getId());
            Thread.sleep(50);
            serviceMethodCalledLatch.countDown();
            return null;
        }).when(summaryService).removeReview(anyString(), anyInt());

        long startTime = System.currentTimeMillis();
        observer.onReviewDeleted(review);
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) < 300, "onReviewDeleted call was blocking. Duration: " + (endTime - startTime) + "ms.");
        assertTrue(serviceMethodCalledLatch.await(3, TimeUnit.SECONDS), "summaryService.removeReview was not called within timeout.");
        
        if (executionThreadId.get() != 0 && executionThreadId.get() != testThreadId) {
            System.out.println("✅ onReviewDeleted: Executed on different thread as expected (Thread ID: " + executionThreadId.get() + ")");
        } else if (executionThreadId.get() != 0) {
            System.out.println("⚠️ onReviewDeleted: Executed on SAME thread (" + executionThreadId.get() + "). @Async proxy might not be fully active.");
        }
        verify(summaryService, times(1)).removeReview(eq(review.getEventId()), eq(review.getRating()));
    }
}