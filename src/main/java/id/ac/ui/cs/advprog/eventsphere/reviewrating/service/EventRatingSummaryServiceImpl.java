package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.EventRatingSummary;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.EventRatingSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventRatingSummaryServiceImpl implements EventRatingSummaryService {

    private final EventRatingSummaryRepository summaryRepository;

    @Override
    public void addReview(String eventId, int rating) {
        log.info("Adding review to rating summary. Event: {}, Rating: {}", eventId, rating);
        
        EventRatingSummary summary = getOrCreateSummary(eventId);
        int oldTotal = summary.getTotalReviews();
        double oldAverage = summary.getAverageRating();
        
        summary.addReview(rating);
        summaryRepository.save(summary);
        
        log.info("Rating summary updated. Event: {}, TotalReviews: {} -> {}, AverageRating: {:.2f} -> {:.2f}", 
                eventId, oldTotal, summary.getTotalReviews(), oldAverage, summary.getAverageRating());
    }

    @Override
    public void updateReview(String eventId, int oldRating, int newRating) {
        log.info("Updating review in rating summary. Event: {}, OldRating: {}, NewRating: {}", 
                eventId, oldRating, newRating);
        
        EventRatingSummary summary = getOrCreateSummary(eventId);
        double oldAverage = summary.getAverageRating();
        
        summary.updateReview(oldRating, newRating);
        summaryRepository.save(summary);
        
        log.info("Rating summary updated after review change. Event: {}, AverageRating: {:.2f} -> {:.2f}", 
                eventId, oldAverage, summary.getAverageRating());
    }

    @Override
    public void removeReview(String eventId, int rating) {
        log.info("Removing review from rating summary. Event: {}, Rating: {}", eventId, rating);
        
        EventRatingSummary summary = getOrCreateSummary(eventId);
        int oldTotal = summary.getTotalReviews();
        double oldAverage = summary.getAverageRating();
        
        summary.removeReview(rating);
        summaryRepository.save(summary);
        
        log.info("Rating summary updated after review removal. Event: {}, TotalReviews: {} -> {}, AverageRating: {:.2f} -> {:.2f}", 
                eventId, oldTotal, summary.getTotalReviews(), oldAverage, summary.getAverageRating());
    }

    @Override
    @Transactional(readOnly = true)
    public double getAverageRating(String eventId) {
        log.debug("Getting average rating for event: {}", eventId);
        
        Optional<EventRatingSummary> summaryOptional = summaryRepository.findByEventId(eventId);
        double averageRating = summaryOptional.map(EventRatingSummary::getAverageRating).orElse(0.0);
        
        log.debug("Average rating for event {}: {:.2f}", eventId, averageRating);
        return averageRating;
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalReviews(String eventId) {
        log.debug("Getting total reviews for event: {}", eventId);
        
        Optional<EventRatingSummary> summaryOptional = summaryRepository.findByEventId(eventId);
        int totalReviews = summaryOptional.map(EventRatingSummary::getTotalReviews).orElse(0);
        
        log.debug("Total reviews for event {}: {}", eventId, totalReviews);
        return totalReviews;
    }

    private EventRatingSummary getOrCreateSummary(String eventId) {
        Optional<EventRatingSummary> existingSummary = summaryRepository.findByEventId(eventId);
        
        if (existingSummary.isPresent()) {
            log.debug("Found existing rating summary for event: {}", eventId);
            return existingSummary.get();
        } else {
            log.info("Creating new rating summary for event: {}", eventId);
            EventRatingSummary newSummary = new EventRatingSummary(eventId);
            return newSummary;
        }
    }
}