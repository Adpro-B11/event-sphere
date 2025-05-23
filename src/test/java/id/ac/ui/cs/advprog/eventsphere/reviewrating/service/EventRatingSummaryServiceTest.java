package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.EventRatingSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional; // Ensure this is imported

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventRatingSummaryServiceTest {

    @Mock
    private EventRatingSummaryRepository summaryRepository;

    private EventRatingSummaryService summaryService;
    private final String EVENT_ID = "evt_123";
    private EventRatingSummary existingSummary; // Added for clarity in mocks

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        summaryService = new EventRatingSummaryServiceImpl(summaryRepository);

        existingSummary = new EventRatingSummary(EVENT_ID); // Initialize here
    }

    @Test
    void testAddReview() {
        existingSummary.addReview(4); // Setup initial state

        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(Optional.of(existingSummary)); // CORRECTED
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);

        summaryService.addReview(EVENT_ID, 5);

        verify(summaryRepository).save(any(EventRatingSummary.class));
        assertEquals(4.5, summaryService.getAverageRating(EVENT_ID), 0.01);
        assertEquals(2, summaryService.getTotalReviews(EVENT_ID));
    }

    @Test
    void testAddReviewForNewEvent() {
        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(Optional.empty()); // CORRECTED
        
        // When a new summary is created and then saved, mock the save and subsequent find
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(invocation -> {
            EventRatingSummary savedSummary = invocation.getArgument(0);
            // If getAverageRating or getTotalReviews is called after save, it might call findByEventId again
            when(summaryRepository.findByEventId(savedSummary.getEventId())).thenReturn(Optional.of(savedSummary));
            return savedSummary;
        });

        summaryService.addReview(EVENT_ID, 5);

        verify(summaryRepository).save(any(EventRatingSummary.class));
        assertEquals(5.0, summaryService.getAverageRating(EVENT_ID), 0.01);
        assertEquals(1, summaryService.getTotalReviews(EVENT_ID));
    }

    @Test
    void testUpdateReview() {
        existingSummary.addReview(3);
        existingSummary.addReview(4);

        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(Optional.of(existingSummary)); // CORRECTED
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);

        summaryService.updateReview(EVENT_ID, 3, 5);

        verify(summaryRepository).save(any(EventRatingSummary.class));
        assertEquals(4.5, summaryService.getAverageRating(EVENT_ID), 0.01);
        assertEquals(2, summaryService.getTotalReviews(EVENT_ID));
    }

    @Test
    void testRemoveReview() {
        existingSummary.addReview(3);
        existingSummary.addReview(5);

        when(summaryRepository.findByEventId(EVENT_ID)).thenReturn(Optional.of(existingSummary)); // CORRECTED
        when(summaryRepository.save(any(EventRatingSummary.class))).thenAnswer(i -> i.getArguments()[0]);

        summaryService.removeReview(EVENT_ID, 3);

        verify(summaryRepository).save(any(EventRatingSummary.class));
        assertEquals(5.0, summaryService.getAverageRating(EVENT_ID), 0.01);
        assertEquals(1, summaryService.getTotalReviews(EVENT_ID));
    }
}