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
import java.util.Optional; // Ensure this is imported

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

        when(userService.getUsernameById(TEST_USER_ID)).thenReturn("John Doe");

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
        when(reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID))
                .thenReturn(Optional.empty()); // CORRECTED

        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);
        request.setComment("Good event");

        reviewService.createReview(TEST_USER_ID, TEST_EVENT_ID, request);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        // ... other assertions
    }

    @Test
    void testCreateReviewWhenUserAlreadyReviewed() {
        when(reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID))
                .thenReturn(Optional.of(testReview)); // CORRECTED

        CreateReviewRequest request = new CreateReviewRequest();
        assertThrows(IllegalStateException.class, () -> {
            reviewService.createReview(TEST_USER_ID, TEST_EVENT_ID, request);
        });
    }

    @Test
    void testGetReviewsByEventId() {
        when(reviewRepository.findByEventId(TEST_EVENT_ID)).thenReturn(Arrays.asList(testReview));
        List<ReviewDTO> result = reviewService.getReviewsByEventId(TEST_EVENT_ID);
        assertEquals(1, result.size());
        // ... other assertions
    }

    @Test
    void testUpdateReview() {
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview)); // CORRECTED
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        Review oldReviewState = new Review(); // To capture state for notification
        oldReviewState.setId(testReview.getId());
        oldReviewState.setRating(testReview.getRating());
        oldReviewState.setComment(testReview.getComment());
        oldReviewState.setEventId(testReview.getEventId());
        oldReviewState.setUserId(testReview.getUserId());
        oldReviewState.setCreatedAt(testReview.getCreatedAt());


        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");

        reviewService.updateReview(TEST_USER_ID, testReview.getId(), request);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        assertEquals(5, reviewCaptor.getValue().getRating());

        // Verify observer was notified with the state of oldReview before changes
        verify(ratingSubject).notifyReviewUpdated(refEq(oldReviewState, "updatedAt"), any(Review.class));
    }

    @Test
    void testUpdateReviewNotFound() {
        when(reviewRepository.findById("non_existent")).thenReturn(Optional.empty()); // CORRECTED
        UpdateReviewRequest request = new UpdateReviewRequest();
        assertThrows(NotFoundException.class, () -> {
            reviewService.updateReview(TEST_USER_ID, "non_existent", request);
        });
    }

    @Test
    void testUpdateReviewNotAuthorized() {
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview)); // CORRECTED
        UpdateReviewRequest request = new UpdateReviewRequest();
        assertThrows(UnauthorizedException.class, () -> {
            reviewService.updateReview("different_user", testReview.getId(), request);
        });
    }

    @Test
    void testDeleteReview() {
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview)); // CORRECTED

        reviewService.deleteReview(TEST_USER_ID, testReview.getId());

        verify(reviewRepository).delete(eq(testReview)); // CORRECTED: verify delete with the entity
        verify(ratingSubject).notifyReviewDeleted(testReview);
    }

    @Test
    void testDeleteReviewNotFound() {
        when(reviewRepository.findById("non_existent")).thenReturn(Optional.empty()); // CORRECTED
        assertThrows(NotFoundException.class, () -> {
            reviewService.deleteReview(TEST_USER_ID, "non_existent");
        });
    }

    @Test
    void testDeleteReviewNotAuthorized() {
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview)); // CORRECTED
        assertThrows(UnauthorizedException.class, () -> {
            reviewService.deleteReview("different_user", testReview.getId());
        });
    }
}