package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventRatingSummaryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventRatingSummaryRepository eventRatingSummaryRepository;

    private String eventId1;
    private EventRatingSummary summary1;

    @BeforeEach
    void setUp() {
        eventRatingSummaryRepository.deleteAll();

        eventId1 = "evt_sum_" + UUID.randomUUID().toString().substring(0,8);
        summary1 = new EventRatingSummary(eventId1);
        summary1.addReview(5);
        summary1.addReview(4);
        // No entityManager.persist() here for EventRatingSummary if its ID is eventId and it's manually set.
        // We save it directly via repository to test save method.
        // If eventId was @GeneratedValue, then we'd use entityManager.
    }

    @Test
    void testSaveAndFindByEventId_NewSummary() {
        EventRatingSummary newSummary = new EventRatingSummary("evt_new_sum");
        newSummary.addReview(3);

        EventRatingSummary savedSummary = eventRatingSummaryRepository.save(newSummary);
        entityManager.flush(); // Make sure it's written to DB

        assertNotNull(savedSummary);
        assertEquals("evt_new_sum", savedSummary.getEventId());
        assertEquals(1, savedSummary.getTotalReviews());
        assertEquals(3.0, savedSummary.getAverageRating(), 0.01);

        Optional<EventRatingSummary> found = eventRatingSummaryRepository.findByEventId("evt_new_sum");
        assertTrue(found.isPresent());
        assertEquals("evt_new_sum", found.get().getEventId());
        assertEquals(1, found.get().getTotalReviews());
    }

    @Test
    void testSaveAndFindByEventId_UpdateExistingSummary() {
        // First, save the initial summary1
        eventRatingSummaryRepository.save(summary1);
        entityManager.flush();

        Optional<EventRatingSummary> initialFound = eventRatingSummaryRepository.findByEventId(eventId1);
        assertTrue(initialFound.isPresent());
        assertEquals(2, initialFound.get().getTotalReviews());
        assertEquals(4.5, initialFound.get().getAverageRating(), 0.01);

        // Update the summary
        EventRatingSummary summaryToUpdate = initialFound.get();
        summaryToUpdate.addReview(3); // totalReviews = 3, totalRatingSum = 5+4+3=12, avg = 4.0
        EventRatingSummary updatedSummary = eventRatingSummaryRepository.save(summaryToUpdate);
        entityManager.flush();


        assertEquals(eventId1, updatedSummary.getEventId());
        assertEquals(3, updatedSummary.getTotalReviews());
        assertEquals(4.0, updatedSummary.getAverageRating(), 0.01);

        Optional<EventRatingSummary> foundAfterUpdate = eventRatingSummaryRepository.findByEventId(eventId1);
        assertTrue(foundAfterUpdate.isPresent());
        assertEquals(3, foundAfterUpdate.get().getTotalReviews());
        assertEquals(4.0, foundAfterUpdate.get().getAverageRating(), 0.01);
    }

    @Test
    void testFindByEventId_NotFound() {
        Optional<EventRatingSummary> found = eventRatingSummaryRepository.findByEventId("non_existent_event_id_sum");
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteByEventId() {
         // First, save the initial summary1
        eventRatingSummaryRepository.save(summary1);
        entityManager.flush();

        Optional<EventRatingSummary> initialFound = eventRatingSummaryRepository.findByEventId(eventId1);
        assertTrue(initialFound.isPresent());

        eventRatingSummaryRepository.deleteById(eventId1);
        entityManager.flush();

        Optional<EventRatingSummary> foundAfterDelete = eventRatingSummaryRepository.findByEventId(eventId1);
        assertFalse(foundAfterDelete.isPresent());
    }
}