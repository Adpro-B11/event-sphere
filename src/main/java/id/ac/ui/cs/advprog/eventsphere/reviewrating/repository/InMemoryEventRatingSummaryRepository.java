package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryEventRatingSummaryRepository implements EventRatingSummaryRepository {

    private final Map<String, EventRatingSummary> summaries = new HashMap<>();

    @Override
    public EventRatingSummary findByEventId(String eventId) {
        return summaries.get(eventId);
    }

    @Override
    public EventRatingSummary save(EventRatingSummary summary) {
        summaries.put(summary.getEventId(), summary);
        return summary;
    }
}