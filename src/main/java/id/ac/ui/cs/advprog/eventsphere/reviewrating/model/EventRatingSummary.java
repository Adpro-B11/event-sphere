package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "event_rating_summaries")
public class EventRatingSummary {

    @Id
    private String eventId;

    private int totalReviews;
    private double totalRatingSum;

    protected EventRatingSummary() {
    }

    public EventRatingSummary(String eventId) {
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID cannot be null or empty");
        }
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
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        totalReviews++;
        totalRatingSum += rating;
    }

    public void updateReview(int oldRating, int newRating) {
        if (oldRating < 1 || oldRating > 5 || newRating < 1 || newRating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        totalRatingSum = totalRatingSum - oldRating + newRating;
    }

    public void removeReview(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        if (totalReviews > 0) {
            totalReviews--;
            totalRatingSum -= rating;
            if (totalReviews == 0) {
                totalRatingSum = 0;
            }
        }
    }
}