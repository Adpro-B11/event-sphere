package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

public interface EventRatingSummaryService {
    void addReview(String eventId, int rating);
    void updateReview(String eventId, int oldRating, int newRating);
    void removeReview(String eventId, int rating);
    double getAverageRating(String eventId);
    int getTotalReviews(String eventId);
}