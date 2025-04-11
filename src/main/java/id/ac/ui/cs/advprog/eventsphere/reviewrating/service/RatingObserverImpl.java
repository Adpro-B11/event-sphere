package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingObserverImpl implements RatingObserver {

    private final EventRatingSummaryService summaryService;

    @Override
    public void onReviewCreated(Review review) {
        summaryService.addReview(review.getEventId(), review.getRating());
    }

    @Override
    public void onReviewUpdated(Review oldReview, Review newReview) {
        summaryService.updateReview(
                oldReview.getEventId(),
                oldReview.getRating(),
                newReview.getRating()
        );
    }

    @Override
    public void onReviewDeleted(Review review) {
        summaryService.removeReview(review.getEventId(), review.getRating());
    }
}