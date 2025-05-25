package id.ac.ui.cs.advprog.eventsphere.reviewrating.observer;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRatingSubject implements RatingSubject {

    private final List<RatingObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(RatingObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RatingObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyReviewCreated(Review review) {
        for (RatingObserver observer : observers) {
            observer.onReviewCreated(review);
        }
    }

    @Override
    public void notifyReviewUpdated(Review oldReview, Review newReview) {
        for (RatingObserver observer : observers) {
            observer.onReviewUpdated(oldReview, newReview);
        }
    }

    @Override
    public void notifyReviewDeleted(Review review) {
        for (RatingObserver observer : observers) {
            observer.onReviewDeleted(review);
        }
    }
}