package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import lombok.Getter;

@Getter
public class EventRatingSummary {
    private final String eventId;
    private int totalReviews;
    private double totalRatingSum;

    public EventRatingSummary(String eventId) {
        this.eventId = eventId;
        this.totalReviews = 0;
        this.totalRatingSum = 0;
    }

    public double getAverageRating() {
        if (totalReviews == 0) {
            return 0.0;
        }
        return totalRatingSum / totalReviews;
    }

    public void addReview(int rating) {
        totalReviews++;
        totalRatingSum += rating;
    }

    public void updateReview(int oldRating, int newRating) {
        totalRatingSum = totalRatingSum - oldRating + newRating;
    }

    public void removeReview(int rating) {
        if (totalReviews > 0) {
            totalReviews--;
            totalRatingSum -= rating;
        }
    }
}