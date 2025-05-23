package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.auth.service.UserService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewDTOFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RatingSubject ratingSubject;

    @Mock
    private ReviewDTOFactory dtoFactory;

    @Mock
    private UserService userService;

    private ReviewService reviewService;
    private Review testReview;
    private final String TEST_USER_ID = "usr_123";
    private final String TEST_EVENT_ID = "evt_123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reviewService = new ReviewServiceImpl(
                reviewRepository,
                ratingSubject,
                dtoFactory,
                userService
        );

        testReview = new Review();
        testReview.setId("rev_123");
        testReview.setRating(4);
        testReview.setComment("Good event");
        testReview.setCreatedAt(ZonedDateTime.now());
        testReview.setUserId(TEST_USER_ID);
        testReview.setEventId(TEST_EVENT_ID);

        // Mock the user service to return a username
        when(userService.getUsernameById(TEST_USER_ID)).thenReturn("John Doe");

        // Mock DTO factory
        ReviewDTO testDTO = ReviewDTO.builder()
                .id(testReview.getId())
                .rating(testReview.getRating())
                .comment(testReview.getComment())
                .createdAt(testReview.getCreatedAt())
                .userId(testReview.getUserId())
                .username("John Doe")
                .eventId(testReview.getEventId())
                .build();
        when(dtoFactory.createFromReview(any(Review.class), anyString())).thenReturn(testDTO);
    }

    @Test
    void testCreateReview() {
        // Mock repository to return null for findByUserIdAndEventId (user hasn't reviewed yet)
        when(reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID))
                .thenReturn(null);

        // Mock repository save to return the saved review
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);
        request.setComment("Good event");

        ReviewDTO result = reviewService.createReview(TEST_USER_ID, TEST_EVENT_ID, request);

        // Verify review was saved with correct data
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();

        assertEquals(TEST_USER_ID, savedReview.getUserId());
        assertEquals(TEST_EVENT_ID, savedReview.getEventId());
        assertEquals(4, savedReview.getRating());
        assertEquals("Good event", savedReview.getComment());
        assertNotNull(savedReview.getCreatedAt());

        // Verify observer was notified
        verify(ratingSubject).notifyReviewCreated(savedReview);

        // Verify result DTO
        assertNotNull(result);
    }

    @Test
    void testCreateReviewWhenUserAlreadyReviewed() {
        // Mock repository to return an existing review
        when(reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID))
                .thenReturn(testReview);

        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(5);
        request.setComment("Updated review");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reviewService.createReview(TEST_USER_ID, TEST_EVENT_ID, request);
        });

        assertEquals("User has already reviewed this event", exception.getMessage());
    }

    @Test
    void testGetReviewsByEventId() {
        List<Review> eventReviews = Arrays.asList(testReview);

        // Mock repository findByEventId
        when(reviewRepository.findByEventId(TEST_EVENT_ID)).thenReturn(eventReviews);

        List<ReviewDTO> result = reviewService.getReviewsByEventId(TEST_EVENT_ID);

        assertEquals(1, result.size());
        verify(dtoFactory).createFromReview(testReview, "John Doe");
    }

    @Test
    void testUpdateReview() {
        // Mock repository findById
        when(reviewRepository.findById(testReview.getId())).thenReturn(testReview);

        // Mock repository save
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review oldReview = new Review();
        oldReview.setId(testReview.getId());
        oldReview.setRating(testReview.getRating());
        oldReview.setComment(testReview.getComment());
        oldReview.setUserId(testReview.getUserId());
        oldReview.setEventId(testReview.getEventId());

        // Create an expected "old review" with the same properties
        Review expectedOldReview = new Review();
        expectedOldReview.setId(testReview.getId());
        expectedOldReview.setRating(testReview.getRating());
        expectedOldReview.setComment(testReview.getComment());
        expectedOldReview.setUserId(testReview.getUserId());
        expectedOldReview.setEventId(testReview.getEventId());
        expectedOldReview.setCreatedAt(testReview.getCreatedAt());

        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");

        ReviewDTO result = reviewService.updateReview(TEST_USER_ID, oldReview.getId(), request);

        // Verify review was updated with correct data
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review updatedReview = reviewCaptor.getValue();

        assertEquals(5, updatedReview.getRating());
        assertEquals("Updated comment", updatedReview.getComment());
        assertNotNull(updatedReview.getUpdatedAt());

        // Verify observer was notified
        verify(ratingSubject).notifyReviewUpdated(refEq(expectedOldReview), any(Review.class));
    }

    @Test
    void testUpdateReviewNotFound() {
        // Mock repository findById to return null
        when(reviewRepository.findById("non_existent")).thenReturn(null);

        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");

        Exception exception = assertThrows(NotFoundException.class, () -> {
            reviewService.updateReview(TEST_USER_ID, "non_existent", request);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testUpdateReviewNotAuthorized() {
        // Mock repository findById
        when(reviewRepository.findById(testReview.getId())).thenReturn(testReview);

        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            reviewService.updateReview("different_user", testReview.getId(), request);
        });

        assertTrue(exception.getMessage().contains("not authorized"));
    }

    @Test
    void testDeleteReview() {
        // Mock repository findById
        when(reviewRepository.findById(testReview.getId())).thenReturn(testReview);

        reviewService.deleteReview(TEST_USER_ID, testReview.getId());

        // Verify review was deleted
        verify(reviewRepository).delete(testReview.getId());

        // Verify observer was notified
        verify(ratingSubject).notifyReviewDeleted(testReview);
    }

    @Test
    void testDeleteReviewNotFound() {
        // Mock repository findById to return null
        when(reviewRepository.findById("non_existent")).thenReturn(null);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            reviewService.deleteReview(TEST_USER_ID, "non_existent");
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testDeleteReviewNotAuthorized() {
        // Mock repository findById
        when(reviewRepository.findById(testReview.getId())).thenReturn(testReview);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            reviewService.deleteReview("different_user", testReview.getId());
        });

        assertTrue(exception.getMessage().contains("not authorized"));
    }
}