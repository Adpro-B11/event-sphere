package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;

public interface EventRatingSummaryRepository {
    EventRatingSummary findByEventId(String eventId);
    EventRatingSummary save(EventRatingSummary summary);
}