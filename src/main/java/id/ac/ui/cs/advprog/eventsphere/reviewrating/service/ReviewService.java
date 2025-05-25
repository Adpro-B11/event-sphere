package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(ReviewRequest request);
    List<ReviewDTO> getReviewsByEventId(String eventId);
    ReviewDTO updateReview(String reviewId, ReviewRequest request);
    void deleteReview(String reviewId, ReviewRequest request);
}