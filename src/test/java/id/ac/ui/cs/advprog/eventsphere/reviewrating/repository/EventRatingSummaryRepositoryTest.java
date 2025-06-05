package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventRatingSummaryRepositoryTest {

    @Mock
    private EventRatingSummaryRepository summaryRepository;

    private EventRatingSummary existingSummary;
    private final String eventId1 = "evt_sum_" + UUID.randomUUID().toString().substring(0, 8);
    private final String eventId2 = "evt_sum_" + UUID.randomUUID().toString().substring(0, 8);

    @BeforeEach
    void setUp() {
        existingSummary = new EventRatingSummary(eventId1);
        existingSummary.addReview(5);
        existingSummary.addReview(4);
        // At this point: totalReviews = 2, averageRating = 4.5
    }

    @Test
    void testSave_NewSummary_ShouldReturnSavedSummary() {
        // Arrange
        EventRatingSummary newSummary = new EventRatingSummary("evt_new_sum");
        newSummary.addReview(3);

        EventRatingSummary savedSummary = new EventRatingSummary("evt_new_sum");
        savedSummary.addReview(3);

        when(summaryRepository.save(any(EventRatingSummary.class))).thenReturn(savedSummary);

        // Act
        EventRatingSummary result = summaryRepository.save(newSummary);

        // Assert
        assertNotNull(result);
        assertEquals("evt_new_sum", result.getEventId());
        assertEquals(1, result.getTotalReviews());
        assertEquals(3.0, result.getAverageRating(), 0.01);

        verify(summaryRepository, times(1)).save(newSummary);
    }

    @Test
    void testSave_UpdateExistingSummary_ShouldReturnUpdatedSummary() {
        // Arrange
        EventRatingSummary summaryToUpdate = new EventRatingSummary(eventId1);
        summaryToUpdate.addReview(5);
        summaryToUpdate.addReview(4);
        summaryToUpdate.addReview(3); // Adding third review: totalReviews = 3, totalSum = 12, avg = 4.0

        when(summaryRepository.save(any(EventRatingSummary.class))).thenReturn(summaryToUpdate);

        // Act
        EventRatingSummary result = summaryRepository.save(summaryToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals(eventId1, result.getEventId());
        assertEquals(3, result.getTotalReviews());
        assertEquals(4.0, result.getAverageRating(), 0.01);

        verify(summaryRepository, times(1)).save(summaryToUpdate);
    }

    @Test
    void testFindByEventId_WhenExists_ShouldReturnSummary() {
        // Arrange
        when(summaryRepository.findByEventId(eventId1)).thenReturn(Optional.of(existingSummary));

        // Act
        Optional<EventRatingSummary> result = summaryRepository.findByEventId(eventId1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(eventId1, result.get().getEventId());
        assertEquals(2, result.get().getTotalReviews());
        assertEquals(4.5, result.get().getAverageRating(), 0.01);

        verify(summaryRepository, times(1)).findByEventId(eventId1);
    }

    @Test
    void testFindByEventId_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        String nonExistentEventId = "non_existent_event_id_sum";
        when(summaryRepository.findByEventId(nonExistentEventId)).thenReturn(Optional.empty());

        // Act
        Optional<EventRatingSummary> result = summaryRepository.findByEventId(nonExistentEventId);

        // Assert
        assertFalse(result.isPresent());
        verify(summaryRepository, times(1)).findByEventId(nonExistentEventId);
    }

    @Test
    void testFindById_WhenExists_ShouldReturnSummary() {
        // Arrange
        when(summaryRepository.findById(eventId1)).thenReturn(Optional.of(existingSummary));

        // Act
        Optional<EventRatingSummary> result = summaryRepository.findById(eventId1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(eventId1, result.get().getEventId());
        assertEquals(2, result.get().getTotalReviews());
        assertEquals(4.5, result.get().getAverageRating(), 0.01);

        verify(summaryRepository, times(1)).findById(eventId1);
    }

    @Test
    void testFindById_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        String nonExistentId = "non_existent_id";
        when(summaryRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<EventRatingSummary> result = summaryRepository.findById(nonExistentId);

        // Assert
        assertFalse(result.isPresent());
        verify(summaryRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testDeleteById_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(summaryRepository).deleteById(eventId1);

        // Act
        summaryRepository.deleteById(eventId1);

        // Assert
        verify(summaryRepository, times(1)).deleteById(eventId1);
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(summaryRepository).delete(existingSummary);

        // Act
        summaryRepository.delete(existingSummary);

        // Assert
        verify(summaryRepository, times(1)).delete(existingSummary);
    }

    @Test
    void testFindAll_ShouldReturnAllSummaries() {
        // Arrange
        EventRatingSummary summary2 = new EventRatingSummary(eventId2);
        summary2.addReview(3);
        summary2.addReview(4);

        List<EventRatingSummary> allSummaries = Arrays.asList(existingSummary, summary2);
        when(summaryRepository.findAll()).thenReturn(allSummaries);

        // Act
        List<EventRatingSummary> result = summaryRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(existingSummary));
        assertTrue(result.contains(summary2));

        verify(summaryRepository, times(1)).findAll();
    }

    @Test
    void testExistsById_WhenExists_ShouldReturnTrue() {
        // Arrange
        when(summaryRepository.existsById(eventId1)).thenReturn(true);

        // Act
        boolean result = summaryRepository.existsById(eventId1);

        // Assert
        assertTrue(result);
        verify(summaryRepository, times(1)).existsById(eventId1);
    }

    @Test
    void testExistsById_WhenNotExists_ShouldReturnFalse() {
        // Arrange
        String nonExistentId = "non_existent_id";
        when(summaryRepository.existsById(nonExistentId)).thenReturn(false);

        // Act
        boolean result = summaryRepository.existsById(nonExistentId);

        // Assert
        assertFalse(result);
        verify(summaryRepository, times(1)).existsById(nonExistentId);
    }

    @Test
    void testCount_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 3L;
        when(summaryRepository.count()).thenReturn(expectedCount);

        // Act
        long result = summaryRepository.count();

        // Assert
        assertEquals(expectedCount, result);
        verify(summaryRepository, times(1)).count();
    }

    @Test
    void testSaveAll_ShouldReturnSavedSummaries() {
        // Arrange
        EventRatingSummary summary2 = new EventRatingSummary(eventId2);
        summary2.addReview(2);

        List<EventRatingSummary> summariesToSave = Arrays.asList(existingSummary, summary2);
        when(summaryRepository.saveAll(summariesToSave)).thenReturn(summariesToSave);

        // Act
        List<EventRatingSummary> result = summaryRepository.saveAll(summariesToSave);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(existingSummary, result.get(0));
        assertEquals(summary2, result.get(1));

        verify(summaryRepository, times(1)).saveAll(summariesToSave);
    }

    @Test
    void testDeleteAll_ShouldCallRepositoryDeleteAll() {
        // Arrange
        doNothing().when(summaryRepository).deleteAll();

        // Act
        summaryRepository.deleteAll();

        // Assert
        verify(summaryRepository, times(1)).deleteAll();
    }

    @Test
    void testFindByEventId_MultipleEventsScenario() {
        // Arrange
        String eventA = "event_A";
        String eventB = "event_B";

        EventRatingSummary summaryA = new EventRatingSummary(eventA);
        summaryA.addReview(5);
        summaryA.addReview(4);

        EventRatingSummary summaryB = new EventRatingSummary(eventB);
        summaryB.addReview(3);

        when(summaryRepository.findByEventId(eventA)).thenReturn(Optional.of(summaryA));
        when(summaryRepository.findByEventId(eventB)).thenReturn(Optional.of(summaryB));

        // Act
        Optional<EventRatingSummary> resultA = summaryRepository.findByEventId(eventA);
        Optional<EventRatingSummary> resultB = summaryRepository.findByEventId(eventB);

        // Assert
        assertTrue(resultA.isPresent());
        assertEquals(eventA, resultA.get().getEventId());
        assertEquals(4.5, resultA.get().getAverageRating(), 0.01);
        assertEquals(2, resultA.get().getTotalReviews());

        assertTrue(resultB.isPresent());
        assertEquals(eventB, resultB.get().getEventId());
        assertEquals(3.0, resultB.get().getAverageRating(), 0.01);
        assertEquals(1, resultB.get().getTotalReviews());

        verify(summaryRepository, times(1)).findByEventId(eventA);
        verify(summaryRepository, times(1)).findByEventId(eventB);
    }
}