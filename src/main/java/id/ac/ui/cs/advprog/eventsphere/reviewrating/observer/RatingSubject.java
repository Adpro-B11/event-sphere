package id.ac.ui.cs.advprog.eventsphere.reviewrating.observer;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;

public interface RatingSubject {
    void addObserver(RatingObserver observer);
    void removeObserver(RatingObserver observer);
    void notifyReviewCreated(Review review);
    void notifyReviewUpdated(Review oldReview, Review newReview);
    void notifyReviewDeleted(Review review);
}