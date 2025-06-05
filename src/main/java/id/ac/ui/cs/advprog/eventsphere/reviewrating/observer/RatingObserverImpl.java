package id.ac.ui.cs.advprog.eventsphere.reviewrating.observer;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.EventRatingSummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RatingObserverImpl implements RatingObserver {

    private final EventRatingSummaryService summaryService;

    @Override
    @Async("taskExecutor")
    public void onReviewCreated(Review review) {
        try {
            summaryService.addReview(review.getEventId(), review.getRating());
            
        } catch (Exception ex) {
        }
    }

    @Override
    @Async("taskExecutor")
    public void onReviewUpdated(Review oldReview, Review newReview) {
        try {
            summaryService.updateReview(
                    oldReview.getEventId(),
                    oldReview.getRating(),
                    newReview.getRating()
            );
        } catch (Exception ex) {
        }
    }

    @Override
    @Async("taskExecutor")
    public void onReviewDeleted(Review review) {
        try {
            summaryService.removeReview(review.getEventId(), review.getRating());
        } catch (Exception ex) {
        }
    }
}