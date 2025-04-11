package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryReviewRepository implements ReviewRepository {

    private final Map<String, Review> reviews = new HashMap<>();

    @Override
    public Review save(Review review) {
        reviews.put(review.getId(), review);
        return review;
    }

    @Override
    public Review findById(String id) {
        return reviews.get(id);
    }

    @Override
    public List<Review> findByEventId(String eventId) {
        return reviews.values().stream()
                .filter(review -> review.getEventId().equals(eventId))
                .collect(Collectors.toList());
    }

    @Override
    public Review findByUserIdAndEventId(String userId, String eventId) {
        return reviews.values().stream()
                .filter(review -> review.getUserId().equals(userId) &&
                        review.getEventId().equals(eventId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        reviews.remove(id);
    }
}