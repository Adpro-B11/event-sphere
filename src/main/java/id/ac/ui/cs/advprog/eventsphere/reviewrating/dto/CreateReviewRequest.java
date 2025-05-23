package id.ac.ui.cs.advprog.eventsphere.reviewrating.dto;

import lombok.Data;

@Data
public class CreateReviewRequest {
    private int rating;
    private String comment;
}