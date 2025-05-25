package id.ac.ui.cs.advprog.eventsphere.reviewrating.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private int rating;
    private String comment;
    private String userId;
    private String username;
    private String eventId;
}