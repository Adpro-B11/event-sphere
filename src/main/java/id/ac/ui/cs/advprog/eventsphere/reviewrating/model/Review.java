package id.ac.ui.cs.advprog.eventsphere.reviewrating.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "reviews", indexes = {
    @Index(name = "idx_review_event_id", columnList = "eventId"),
    @Index(name = "idx_review_user_id_event_id", columnList = "userId, eventId", unique = true)
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int rating;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String comment;

    @Setter
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Setter
    private ZonedDateTime updatedAt;

    @Setter
    @Column(nullable = false)
    private String userId;

    @Setter
    @Column(nullable = false)
    private String username;

    @Setter
    @Column(nullable = false)
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

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = ZonedDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }
}