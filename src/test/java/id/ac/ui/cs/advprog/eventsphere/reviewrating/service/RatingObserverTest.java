package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.*;

class RatingObserverTest {

    @Mock
    private RatingSubject subject;

    @Mock
    private EventRatingSummaryService summaryService;

    private RatingObserverImpl observer;
    private Review review;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        observer = new RatingObserverImpl(summaryService);
        subject.addObserver(observer);

        review = new Review();
        review.setId("rev_123");
        review.setRating(4);
        review.setComment("Good event");
        review.setCreatedAt(ZonedDateTime.now());
        review.setUserId("usr_123");
        review.setEventId("evt_123");
    }

    @Test
    void testOnReviewCreated() {
        observer.onReviewCreated(review);
        verify(summaryService).addReview(review.getEventId(), review.getRating());
    }

    @Test
    void testOnReviewUpdated() {
        Review oldReview = new Review();
        oldReview.setId("rev_123");
        oldReview.setRating(3);
        oldReview.setEventId("evt_123");

        observer.onReviewUpdated(oldReview, review);
        verify(summaryService).updateReview(review.getEventId(), 3, 4);
    }

    @Test
    void testOnReviewDeleted() {
        observer.onReviewDeleted(review);
        verify(summaryService).removeReview(review.getEventId(), review.getRating());
    }
}