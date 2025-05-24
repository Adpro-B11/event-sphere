package id.ac.ui.cs.advprog.eventsphere.reviewrating.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRatingSummaryDTO {
    private String eventId;
    private double averageRating;
    private int totalReviews;
}