package id.ac.ui.cs.advprog.eventsphere.reviewrating.dto;

import lombok.Data;

@Data
public class UpdateReviewRequest {
    private int rating;
    private String comment;
}