package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;

public interface RatingObserver {
    void onReviewCreated(Review review);
    void onReviewUpdated(Review oldReview, Review newReview);
    void onReviewDeleted(Review review);
}