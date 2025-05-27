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
            log.info("Processing review creation event - Review: {}, Event: {}, Rating: {}", 
                    review.getId(), review.getEventId(), review.getRating());
            
            summaryService.addReview(review.getEventId(), review.getRating());
            
            log.info("Successfully processed review creation - Review: {}", review.getId());
        } catch (Exception ex) {
            log.error("Failed to process review creation - Review: {}, Event: {}, Error: {}", 
                    review.getId(), review.getEventId(), ex.getMessage(), ex);
        }
    }

    @Override
    @Async("taskExecutor")
    public void onReviewUpdated(Review oldReview, Review newReview) {
        try {
            log.info("Processing review update event - Review: {}, Event: {}, OldRating: {}, NewRating: {}", 
                    newReview.getId(), newReview.getEventId(), oldReview.getRating(), newReview.getRating());
            
            summaryService.updateReview(
                    oldReview.getEventId(),
                    oldReview.getRating(),
                    newReview.getRating()
            );
            
            log.info("Successfully processed review update - Review: {}", newReview.getId());
        } catch (Exception ex) {
            log.error("Failed to process review update - Review: {}, Event: {}, Error: {}", 
                    newReview.getId(), newReview.getEventId(), ex.getMessage(), ex);
        }
    }

    @Override
    @Async("taskExecutor")
    public void onReviewDeleted(Review review) {
        try {
            log.info("Processing review deletion event - Review: {}, Event: {}, Rating: {}", 
                    review.getId(), review.getEventId(), review.getRating());
            
            summaryService.removeReview(review.getEventId(), review.getRating());
            
            log.info("Successfully processed review deletion - Review: {}", review.getId());
        } catch (Exception ex) {
            log.error("Failed to process review deletion - Review: {}, Event: {}, Error: {}", 
                    review.getId(), review.getEventId(), ex.getMessage(), ex);
        }
    }
}