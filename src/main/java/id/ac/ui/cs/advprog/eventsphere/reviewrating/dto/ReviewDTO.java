package id.ac.ui.cs.advprog.eventsphere.reviewrating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private String id;
    private int rating;
    private String comment;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String userId;
    private String username;
    private String eventId;
}