package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(String userId, String eventId, CreateReviewRequest request);
    List<ReviewDTO> getReviewsByEventId(String eventId);
    ReviewDTO updateReview(String userId, String reviewId, UpdateReviewRequest request);
    void deleteReview(String userId, String reviewId);
    }