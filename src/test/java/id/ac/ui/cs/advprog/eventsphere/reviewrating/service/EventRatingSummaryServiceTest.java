package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.EventRatingSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventRatingSummaryServiceTest {

    @Mock
    private EventRatingSummaryRepository summaryRepository;

    private EventRatingSummaryService summaryService;
    private final String EVENT_ID = "evt_123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        summaryService = new EventRatingSummaryServiceImpl(summaryRepository);
    }

    @Test
    void testAddReview() {
        // Mock an existing summary
        EventRatingSummary existingSummary = new EventRatingSummary(EVENT_ID);
        existingSummary.addReview(4); // Already has one review with rating 4

        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(existingSummary);
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);

        summaryService.addReview(EVENT_ID, 5);

        // Verify summary was updated
        verify(summaryRepository).save(any(EventRatingSummary.class));

        // The average should be (4+5)/2 = 4.5
        assertEquals(4.5, summaryService.getAverageRating(EVENT_ID), 0.01);
        assertEquals(2, summaryService.getTotalReviews(EVENT_ID));
    }

//    @Test
//    void testAddReviewForNewEvent() {
//        // Mock no existing summary
//        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(null);
//        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);
//
//        summaryService.addReview(EVENT_ID, 5);
//
//        // Verify a new summary was created and saved
//        verify(summaryRepository).save(any(EventRatingSummary.class));
//
//        // The average should be 5.0 (just one review)
//        assertEquals(5.0, summaryService.getAverageRating(EVENT_ID), 0.01);
//        assertEquals(1, summaryService.getTotalReviews(EVENT_ID));
//    }

    @Test
    void testUpdateReview() {
        // Mock an existing summary
        EventRatingSummary existingSummary = new EventRatingSummary(EVENT_ID);
        existingSummary.addReview(3);
        existingSummary.addReview(4);

        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(existingSummary);
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);

        // Update one review from rating 3 to rating 5
        summaryService.updateReview(EVENT_ID, 3, 5);

        // Verify summary was updated
        verify(summaryRepository).save(any(EventRatingSummary.class));

        // The average should change from (3+4)/2 = 3.5 to (5+4)/2 = 4.5
        assertEquals(4.5, summaryService.getAverageRating(EVENT_ID), 0.01);
        // Total reviews should stay the same
        assertEquals(2, summaryService.getTotalReviews(EVENT_ID));
    }

    @Test
    void testRemoveReview() {
        // Mock an existing summary
        EventRatingSummary existingSummary = new EventRatingSummary(EVENT_ID);
        existingSummary.addReview(3);
        existingSummary.addReview(5);

        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(existingSummary);
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);

        // Remove the review with rating 3
        summaryService.removeReview(EVENT_ID, 3);

        // Verify summary was updated
        verify(summaryRepository).save(any(EventRatingSummary.class));

        // The average should change from (3+5)/2 = 4.0 to just 5.0
        assertEquals(5.0, summaryService.getAverageRating(EVENT_ID), 0.01);
        // Total reviews should decrease by 1
        assertEquals(1, summaryService.getTotalReviews(EVENT_ID));
    }
}