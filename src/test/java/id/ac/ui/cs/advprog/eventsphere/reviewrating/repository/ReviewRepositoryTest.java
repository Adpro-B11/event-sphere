package id.ac.ui.cs.advprog.eventsphere.reviewrating.repository;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewRepositoryTest {

    @Mock
    private ReviewRepository reviewRepository;

    private Review testReview1;
    private Review testReview2;
    private final String eventId1 = "evt_" + UUID.randomUUID().toString().substring(0, 8);
    private final String userId1 = "usr_" + UUID.randomUUID().toString().substring(0, 8);
    private final String username1 = "John Doe";
    private final String eventId2 = "evt_" + UUID.randomUUID().toString().substring(0, 8);
    private final String userId2 = "usr_" + UUID.randomUUID().toString().substring(0, 8);
    private final String username2 = "Jane Smith";

    @BeforeEach
    void setUp() {
        testReview1 = new Review();
        testReview1.setId("review_1");
        testReview1.setRating(4);
        testReview1.setComment("Good event");
        testReview1.setCreatedAt(ZonedDateTime.now());
        testReview1.setUserId(userId1);
        testReview1.setUsername(username1);
        testReview1.setEventId(eventId1);
        
        testReview2 = new Review();
        testReview2.setId("review_2");
        testReview2.setRating(5);
        testReview2.setComment("Excellent event!");
        testReview2.setCreatedAt(ZonedDateTime.now().minusDays(1));
        testReview2.setUserId(userId2);
        testReview2.setUsername(username2); 
        testReview2.setEventId(eventId1); // Same event as review1
    }

    @Test
    void testSave_ShouldReturnSavedReview() {
        // Arrange
        Review newReview = new Review();
        newReview.setRating(3);
        newReview.setComment("Okay event");
        newReview.setCreatedAt(ZonedDateTime.now());
        newReview.setUserId("usr_new");
        newReview.setUsername("New User");
        newReview.setEventId("evt_new");

        Review savedReview = new Review();
        savedReview.setId("generated_id_123");
        savedReview.setRating(3);
        savedReview.setComment("Okay event");
        savedReview.setCreatedAt(newReview.getCreatedAt());
        savedReview.setUserId("usr_new");
        savedReview.setUsername("New User");
        savedReview.setEventId("evt_new");

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        // Act
        Review result = reviewRepository.save(newReview);

        // Assert
        assertNotNull(result);
        assertEquals("generated_id_123", result.getId());
        assertEquals(3, result.getRating());
        assertEquals("New User", result.getUsername());
        assertEquals("evt_new", result.getEventId());
        
        verify(reviewRepository, times(1)).save(newReview);
    }

    @Test
    void testFindById_WhenExists_ShouldReturnReview() {
        // Arrange
        String reviewId = "review_1";
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(testReview1));

        // Act
        Optional<Review> result = reviewRepository.findById(reviewId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testReview1.getId(), result.get().getId());
        assertEquals(4, result.get().getRating());
        assertEquals(username1, result.get().getUsername());
        
        verify(reviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void testFindById_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        String nonExistentId = "non_existent_id";
        when(reviewRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act
        Optional<Review> result = reviewRepository.findById(nonExistentId);

        // Assert
        assertFalse(result.isPresent());
        verify(reviewRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testFindByEventId_WhenReviewsExist_ShouldReturnReviews() {
        // Arrange
        List<Review> expectedReviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepository.findByEventId(eventId1)).thenReturn(expectedReviews);

        // Act
        List<Review> result = reviewRepository.findByEventId(eventId1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(r -> r.getComment().equals("Good event")));
        assertTrue(result.stream().anyMatch(r -> r.getComment().equals("Excellent event!")));
        assertTrue(result.stream().anyMatch(r -> r.getUsername().equals(username1)));
        assertTrue(result.stream().anyMatch(r -> r.getUsername().equals(username2)));
        
        verify(reviewRepository, times(1)).findByEventId(eventId1);
    }

    @Test
    void testFindByEventId_WhenNoReviews_ShouldReturnEmptyList() {
        // Arrange
        String eventIdWithNoReviews = "evt_no_reviews";
        when(reviewRepository.findByEventId(eventIdWithNoReviews)).thenReturn(Collections.emptyList());

        // Act
        List<Review> result = reviewRepository.findByEventId(eventIdWithNoReviews);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findByEventId(eventIdWithNoReviews);
    }

    @Test
    void testFindByUserIdAndEventId_WhenExists_ShouldReturnReview() {
        // Arrange
        when(reviewRepository.findByUserIdAndEventId(userId1, eventId1)).thenReturn(Optional.of(testReview1));

        // Act
        Optional<Review> result = reviewRepository.findByUserIdAndEventId(userId1, eventId1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testReview1.getId(), result.get().getId());
        assertEquals(4, result.get().getRating());
        assertEquals(username1, result.get().getUsername());
        
        verify(reviewRepository, times(1)).findByUserIdAndEventId(userId1, eventId1);
    }

    @Test
    void testFindByUserIdAndEventId_WhenNotExists_ShouldReturnEmpty() {
        // Arrange
        String nonExistentUserId = "non_existent_user";
        String nonExistentEventId = "non_existent_event";
        when(reviewRepository.findByUserIdAndEventId(nonExistentUserId, nonExistentEventId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Review> result = reviewRepository.findByUserIdAndEventId(nonExistentUserId, nonExistentEventId);

        // Assert
        assertFalse(result.isPresent());
        verify(reviewRepository, times(1)).findByUserIdAndEventId(nonExistentUserId, nonExistentEventId);
    }

    @Test
    void testDeleteById_ShouldCallRepositoryDelete() {
        // Arrange
        String reviewId = "review_to_delete";
        doNothing().when(reviewRepository).deleteById(reviewId);

        // Act
        reviewRepository.deleteById(reviewId);

        // Assert
        verify(reviewRepository, times(1)).deleteById(reviewId);
    }

    @Test
    void testDelete_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(reviewRepository).delete(testReview1);

        // Act
        reviewRepository.delete(testReview1);

        // Assert
        verify(reviewRepository, times(1)).delete(testReview1);
    }

    @Test
    void testFindAll_ShouldReturnAllReviews() {
        // Arrange
        List<Review> allReviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepository.findAll()).thenReturn(allReviews);

        // Act
        List<Review> result = reviewRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testReview1));
        assertTrue(result.contains(testReview2));
        
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testExistsById_WhenExists_ShouldReturnTrue() {
        // Arrange
        String existingId = "review_1";
        when(reviewRepository.existsById(existingId)).thenReturn(true);

        // Act
        boolean result = reviewRepository.existsById(existingId);

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).existsById(existingId);
    }

    @Test
    void testExistsById_WhenNotExists_ShouldReturnFalse() {
        // Arrange
        String nonExistentId = "non_existent_id";
        when(reviewRepository.existsById(nonExistentId)).thenReturn(false);

        // Act
        boolean result = reviewRepository.existsById(nonExistentId);

        // Assert
        assertFalse(result);
        verify(reviewRepository, times(1)).existsById(nonExistentId);
    }

    @Test
    void testCount_ShouldReturnCorrectCount() {
        // Arrange
        long expectedCount = 5L;
        when(reviewRepository.count()).thenReturn(expectedCount);

        // Act
        long result = reviewRepository.count();

        // Assert
        assertEquals(expectedCount, result);
        verify(reviewRepository, times(1)).count();
    }
}