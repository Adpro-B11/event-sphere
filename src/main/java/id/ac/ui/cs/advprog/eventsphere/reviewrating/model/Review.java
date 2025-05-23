package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
public class Review {
    private String id;
    private int rating;
    @Setter
    private String comment;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    @Setter
    private String userId;
    @Setter
    private String eventId;

    public void setId(String id) {
        this.id = id;
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}