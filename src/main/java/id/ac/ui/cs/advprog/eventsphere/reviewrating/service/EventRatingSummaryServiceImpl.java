package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.EventRatingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventRatingSummaryServiceImpl implements EventRatingSummaryService {

    private final EventRatingSummaryRepository summaryRepository;

    @Override
    public void addReview(String eventId, int rating) {
        EventRatingSummary summary = getOrCreateSummary(eventId);
        summary.addReview(rating);
        summaryRepository.save(summary);
    }

    @Override
    public void updateReview(String eventId, int oldRating, int newRating) {
        EventRatingSummary summary = getOrCreateSummary(eventId);
        summary.updateReview(oldRating, newRating);
        summaryRepository.save(summary);
    }

    @Override
    public void removeReview(String eventId, int rating) {
        EventRatingSummary summary = getOrCreateSummary(eventId);
        summary.removeReview(rating);
        summaryRepository.save(summary);
    }

    @Override
    public double getAverageRating(String eventId) {
        EventRatingSummary summary = summaryRepository.findByEventId(eventId);
        return summary != null ? summary.getAverageRating() : 0.0;
    }

    @Override
    public int getTotalReviews(String eventId) {
        EventRatingSummary summary = summaryRepository.findByEventId(eventId);
        return summary != null ? summary.getTotalReviews() : 0;
    }

    private EventRatingSummary getOrCreateSummary(String eventId) {
        EventRatingSummary summary = summaryRepository.findByEventId(eventId);
        if (summary == null) {
            summary = new EventRatingSummary(eventId);
        }
        return summary;
    }
}