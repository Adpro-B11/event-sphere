package id.ac.ui.cs.advprog.eventsphere.reviewrating.observer;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.EventRatingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingObserverImpl implements RatingObserver {

    private final EventRatingSummaryService summaryService;

    @Override
    @Async("taskExecutor")
    public void onReviewCreated(Review review) {
        summaryService.addReview(review.getEventId(), review.getRating());
    }

    @Override
    @Async("taskExecutor")
    public void onReviewUpdated(Review oldReview, Review newReview) {
        summaryService.updateReview(
                oldReview.getEventId(),
                oldReview.getRating(),
                newReview.getRating()
        );
    }

    @Override
    @Async("taskExecutor")
    public void onReviewDeleted(Review review) {
        summaryService.removeReview(review.getEventId(), review.getRating());
    }
}