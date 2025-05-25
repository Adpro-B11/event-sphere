package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.EventRatingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
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
    @Transactional(readOnly = true)
    public double getAverageRating(String eventId) {
        Optional<EventRatingSummary> summaryOptional = summaryRepository.findByEventId(eventId);
        return summaryOptional.map(EventRatingSummary::getAverageRating).orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalReviews(String eventId) {
        Optional<EventRatingSummary> summaryOptional = summaryRepository.findByEventId(eventId);
        return summaryOptional.map(EventRatingSummary::getTotalReviews).orElse(0);
    }

    private EventRatingSummary getOrCreateSummary(String eventId) {
        return summaryRepository.findByEventId(eventId)
                .orElseGet(() -> {
                    EventRatingSummary newSummary = new EventRatingSummary(eventId);
                    return newSummary;
                });
    }
}