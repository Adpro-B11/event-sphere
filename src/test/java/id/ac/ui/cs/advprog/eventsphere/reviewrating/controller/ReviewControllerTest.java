package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.EventRatingSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReviewService reviewService;

    @Mock
    private EventRatingSummaryService ratingSummaryService;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    private String eventId = "evt_123";
    private String userId = "usr_123";
    private String username = "testuser";
    private String reviewId = "rev_123";
    private ReviewDTO sampleReviewDTO;
    private List<ReviewDTO> reviewList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setAsyncRequestTimeout(5000)
                .build();
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        sampleReviewDTO = ReviewDTO.builder()
                .id(reviewId)
                .rating(4)
                .comment("Good event")
                .createdAt(ZonedDateTime.now())
                .userId(userId)
                .username(username)
                .eventId(eventId)
                .build();
        reviewList = Arrays.asList(sampleReviewDTO);
    }

    @Test
    void testGetReviewsByEventId_Success() throws Exception {
        given(reviewService.getReviewsByEventId(eventId)).willReturn(reviewList);

        MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(reviewId)))
                .andExpect(jsonPath("$[0].rating", is(4)))
                .andExpect(jsonPath("$[0].username", is(username)));
                
        verify(reviewService).getReviewsByEventId(eventId);
    }

    @Test
    void testGetReviewsByEventId_EventNotFinished() throws Exception {
        given(reviewService.getReviewsByEventId(eventId))
                .willThrow(new IllegalStateException("Reviews can only be viewed for finished events"));

        MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
                
        verify(reviewService).getReviewsByEventId(eventId);
    }

    @Test
    void testCreateReview_Success() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Good event");
        requestBody.setUserId(userId);
        requestBody.setUsername(username);
        requestBody.setEventId(eventId);

        given(reviewService.createReview(any(ReviewRequest.class)))
                .willReturn(sampleReviewDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(reviewId)))
                .andExpect(jsonPath("$.rating", is(4)))
                .andExpect(jsonPath("$.comment", is("Good event")));
                
        verify(reviewService).createReview(any(ReviewRequest.class));
    }

    @Test
    void testCreateReview_EventNotFinished() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Good event");
        requestBody.setUserId(userId);
        requestBody.setUsername(username);
        requestBody.setEventId(eventId);

        given(reviewService.createReview(any(ReviewRequest.class)))
                .willThrow(new IllegalStateException("Reviews can only be created for finished events"));

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isBadRequest());
                
        verify(reviewService).createReview(any(ReviewRequest.class));
    }

    @Test
    void testCreateReview_UserHasNoTicket() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Good event");
        requestBody.setUserId(userId);
        requestBody.setUsername(username);
        requestBody.setEventId(eventId);

        given(reviewService.createReview(any(ReviewRequest.class)))
                .willThrow(new UnauthorizedException("Only users with tickets can review this event"));

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isForbidden());
                
        verify(reviewService).createReview(any(ReviewRequest.class));
    }

    @Test
    void testCreateReview_UserAlreadyReviewed() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Trying to review again");
        requestBody.setUserId(userId);
        requestBody.setUsername(username);
        requestBody.setEventId(eventId);

        given(reviewService.createReview(any(ReviewRequest.class)))
                .willThrow(new IllegalStateException("User has already reviewed this event"));

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isConflict());
                
        verify(reviewService).createReview(any(ReviewRequest.class));
    }

    @Test
    void testUpdateReview_Success() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(5);
        requestBody.setComment("Updated: Excellent event!");
        requestBody.setUserId(userId);
        requestBody.setUsername(username);
        requestBody.setEventId(eventId);
        
        ReviewDTO updatedReviewDTO = ReviewDTO.builder()
                .id(reviewId).rating(5).comment("Updated: Excellent event!")
                .createdAt(sampleReviewDTO.getCreatedAt()).updatedAt(ZonedDateTime.now())
                .userId(userId).username(username).eventId(eventId).build();

        given(reviewService.updateReview(eq(reviewId), any(ReviewRequest.class)))
                .willReturn(updatedReviewDTO);

        MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reviewId)))
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.comment", is("Updated: Excellent event!")));
                
        verify(reviewService).updateReview(eq(reviewId), any(ReviewRequest.class));
    }

    @Test
    void testUpdateReview_NotFound() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(5);
        requestBody.setUserId(userId);
        requestBody.setEventId(eventId);
        String nonExistentReviewId = "nonexistent_rev_id";

        given(reviewService.updateReview(eq(nonExistentReviewId), any(ReviewRequest.class)))
                .willThrow(new NotFoundException("Review not found"));

        MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
                
        verify(reviewService).updateReview(eq(nonExistentReviewId), any(ReviewRequest.class));
    }

    @Test
    void testUpdateReview_Unauthorized() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setRating(5);
        requestBody.setUserId(userId);
        requestBody.setEventId(eventId);

        given(reviewService.updateReview(eq(reviewId), any(ReviewRequest.class)))
                .willThrow(new UnauthorizedException("Not authorized"));

        MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isForbidden());
                
        verify(reviewService).updateReview(eq(reviewId), any(ReviewRequest.class));
    }

    @Test
    void testDeleteReview_Success() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setUserId(userId);
        requestBody.setEventId(eventId);

        doNothing().when(reviewService).deleteReview(eq(reviewId), any(ReviewRequest.class));

        MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent());
                
        verify(reviewService).deleteReview(eq(reviewId), any(ReviewRequest.class));
    }

    @Test
    void testDeleteReview_NotFound() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setUserId(userId);
        requestBody.setEventId(eventId);
        String nonExistentReviewId = "nonexistent_rev_id";
        
        doThrow(new NotFoundException("Review not found"))
                .when(reviewService).deleteReview(eq(nonExistentReviewId), any(ReviewRequest.class));

        MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
                
        verify(reviewService).deleteReview(eq(nonExistentReviewId), any(ReviewRequest.class));
    }

    @Test
    void testDeleteReview_Unauthorized() throws Exception {
        ReviewRequest requestBody = new ReviewRequest();
        requestBody.setUserId(userId);
        requestBody.setEventId(eventId);
        
        doThrow(new UnauthorizedException("Not authorized"))
                .when(reviewService).deleteReview(eq(reviewId), any(ReviewRequest.class));

        MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isForbidden());
                
        verify(reviewService).deleteReview(eq(reviewId), any(ReviewRequest.class));
    }

    @Test
    void testGetEventRatingSummary() throws Exception {
        double expectedAverageRating = 4.2;
        int expectedTotalReviews = 5;

        given(ratingSummaryService.getAverageRating(eventId)).willReturn(expectedAverageRating);
        given(ratingSummaryService.getTotalReviews(eventId)).willReturn(expectedTotalReviews);

        MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/rating-summary", eventId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(eventId)))
                .andExpect(jsonPath("$.averageRating", is(expectedAverageRating)))
                .andExpect(jsonPath("$.totalReviews", is(expectedTotalReviews)));

        verify(ratingSummaryService).getAverageRating(eventId);
        verify(ratingSummaryService).getTotalReviews(eventId);
    }
}