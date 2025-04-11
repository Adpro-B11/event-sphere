package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import java.util.ArrayList;
import java.util.List;

public interface RatingSubject {
    void addObserver(RatingObserver observer);
    void removeObserver(RatingObserver observer);
    void notifyReviewCreated(Review review);
    void notifyReviewUpdated(Review oldReview, Review newReview);
    void notifyReviewDeleted(Review review);
}