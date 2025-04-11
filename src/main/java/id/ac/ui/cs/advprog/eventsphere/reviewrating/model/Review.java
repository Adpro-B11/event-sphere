// src/main/java/id/ac/ui/cs/advprog/eventsphere/reviewrating/model/Review.java
package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
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
    private User user;
    private Event event;

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

    public void setUser(User user) {
        this.user = user;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}