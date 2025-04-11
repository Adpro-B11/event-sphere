package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;

import java.util.List;

public interface ReviewRepository {
    Review save(Review review);
    Review findById(String id);
    List<Review> findByEventId(String eventId);
    Review findByUserIdAndEventId(String userId, String eventId);
    void delete(String id);
}