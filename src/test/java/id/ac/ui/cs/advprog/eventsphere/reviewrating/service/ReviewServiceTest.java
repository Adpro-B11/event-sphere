package id.ac.ui.cs.advprog.eventsphere.reviewrating.service;

import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.factory.ReviewDTOFactory;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.model.Review;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.repository.ReviewRepository;
import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private TicketService ticketService;

    @Mock
    private EventService eventService;

    private ReviewService reviewService;
    private Review testReview;
    private final String TEST_USER_ID = "usr_123";
    private final String TEST_EVENT_ID = "evt_123";
    private final String TEST_USERNAME = "John Doe";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reviewService = new ReviewServiceImpl(
                reviewRepository,
                ratingSubject,
                dtoFactory,
                ticketService,
                eventService
        );

        testReview = new Review();
        testReview.setId("rev_123");
        testReview.setRating(4);
        testReview.setComment("Good event");
        testReview.setCreatedAt(ZonedDateTime.now());
        testReview.setUserId(TEST_USER_ID);
        testReview.setUsername(TEST_USERNAME);
        testReview.setEventId(TEST_EVENT_ID);

        ReviewDTO testDTO = ReviewDTO.builder()
                .id(testReview.getId())
                .rating(testReview.getRating())
                .comment(testReview.getComment())
                .createdAt(testReview.getCreatedAt())
                .userId(testReview.getUserId())
                .username(testReview.getUsername())
                .eventId(testReview.getEventId())
                .build();
        when(dtoFactory.createFromReview(any(Review.class))).thenReturn(testDTO);
    }

    @Test
    void testCreateReview_Success() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(ticketService.userHasTicket(TEST_USER_ID, TEST_EVENT_ID)).thenReturn(true);
        when(reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID))
                .thenReturn(Optional.empty());
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        ReviewRequest request = new ReviewRequest();
        request.setRating(4);
        request.setComment("Good event");
        request.setUserId(TEST_USER_ID);
        request.setUsername(TEST_USERNAME);
        request.setEventId(TEST_EVENT_ID);

        reviewService.createReview(request);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        Review savedReview = reviewCaptor.getValue();
        
        assertEquals(4, savedReview.getRating());
        assertEquals("Good event", savedReview.getComment());
        assertEquals(TEST_USER_ID, savedReview.getUserId());
        assertEquals(TEST_USERNAME, savedReview.getUsername());
        assertEquals(TEST_EVENT_ID, savedReview.getEventId());
        verify(ratingSubject).notifyReviewCreated(any(Review.class));
    }

    @Test
    void testCreateReview_EventNotFinished() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(false);

        ReviewRequest request = new ReviewRequest();
        request.setEventId(TEST_EVENT_ID);
        request.setUserId(TEST_USER_ID);

        assertThrows(IllegalStateException.class, () -> {
            reviewService.createReview(request);
        });
        
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testCreateReview_UserHasNoTicket() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(ticketService.userHasTicket(TEST_USER_ID, TEST_EVENT_ID)).thenReturn(false);

        ReviewRequest request = new ReviewRequest();
        request.setEventId(TEST_EVENT_ID);
        request.setUserId(TEST_USER_ID);

        assertThrows(UnauthorizedException.class, () -> {
            reviewService.createReview(request);
        });
        
        verify(reviewRepository, never()).save(any());
    }

    @Test
    void testCreateReview_UserAlreadyReviewed() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(ticketService.userHasTicket(TEST_USER_ID, TEST_EVENT_ID)).thenReturn(true);
        when(reviewRepository.findByUserIdAndEventId(TEST_USER_ID, TEST_EVENT_ID))
                .thenReturn(Optional.of(testReview));

        ReviewRequest request = new ReviewRequest();
        request.setEventId(TEST_EVENT_ID);
        request.setUserId(TEST_USER_ID);

        assertThrows(IllegalStateException.class, () -> {
            reviewService.createReview(request);
        });
    }

    @Test
    void testGetReviewsByEventId_EventNotFinished() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> {
            reviewService.getReviewsByEventId(TEST_EVENT_ID);
        });
    }

    @Test
    void testGetReviewsByEventId_Success() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(reviewRepository.findByEventId(TEST_EVENT_ID)).thenReturn(Arrays.asList(testReview));

        List<ReviewDTO> result = reviewService.getReviewsByEventId(TEST_EVENT_ID);
        
        assertEquals(1, result.size());
        verify(dtoFactory).createFromReview(testReview);
    }

    @Test
    void testUpdateReview_Success() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(ticketService.userHasTicket(TEST_USER_ID, TEST_EVENT_ID)).thenReturn(true);
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview));
        when(reviewRepository.save(any(Review.class))).thenAnswer(i -> i.getArguments()[0]);

        ReviewRequest request = new ReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");
        request.setUserId(TEST_USER_ID);
        request.setEventId(TEST_EVENT_ID);

        reviewService.updateReview(testReview.getId(), request);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());
        assertEquals(5, reviewCaptor.getValue().getRating());
        verify(ratingSubject).notifyReviewUpdated(any(Review.class), any(Review.class));
    }

    @Test
    void testUpdateReview_NotAuthorized() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(ticketService.userHasTicket("different_user", TEST_EVENT_ID)).thenReturn(true);
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview));

        ReviewRequest request = new ReviewRequest();
        request.setUserId("different_user");
        request.setEventId(TEST_EVENT_ID);

        assertThrows(UnauthorizedException.class, () -> {
            reviewService.updateReview(testReview.getId(), request);
        });
    }

    @Test
    void testDeleteReview_Success() {
        when(eventService.isEventFinished(TEST_EVENT_ID)).thenReturn(true);
        when(ticketService.userHasTicket(TEST_USER_ID, TEST_EVENT_ID)).thenReturn(true);
        when(reviewRepository.findById(testReview.getId())).thenReturn(Optional.of(testReview));

        ReviewRequest request = new ReviewRequest();
        request.setUserId(TEST_USER_ID);
        request.setEventId(TEST_EVENT_ID);

        reviewService.deleteReview(testReview.getId(), request);

        verify(reviewRepository).delete(eq(testReview));
        verify(ratingSubject).notifyReviewDeleted(testReview);
    }

    @Test
    void testDeleteReview_NotFound() {
        when(reviewRepository.findById("non_existent")).thenReturn(Optional.empty());
        
        ReviewRequest request = new ReviewRequest();
        request.setUserId(TEST_USER_ID);
        
        assertThrows(NotFoundException.class, () -> {
            reviewService.deleteReview("non_existent", request);
        });
    }
}