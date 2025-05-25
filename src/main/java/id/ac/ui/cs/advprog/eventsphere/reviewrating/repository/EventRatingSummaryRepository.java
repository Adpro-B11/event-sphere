package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRatingSummaryRepository extends JpaRepository<EventRatingSummary, String> {
    Optional<EventRatingSummary> findByEventId(String eventId);
}