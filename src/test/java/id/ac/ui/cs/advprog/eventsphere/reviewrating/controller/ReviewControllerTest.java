package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
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

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    private String eventId = "evt_123";
    private String userId = "usr_123";
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
                .username("User 1")
                .eventId(eventId)
                .build();
        reviewList = Arrays.asList(sampleReviewDTO);
    }

    @Test
    void testGetReviewsByEventId() throws Exception {
        given(reviewService.getReviewsByEventId(eventId)).willReturn(reviewList);

        MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(reviewId)));
                
        verify(reviewService).getReviewsByEventId(eventId);
    }

    @Test
    void testGetReviewsByEventId_EmptyList() throws Exception {
        given(reviewService.getReviewsByEventId(eventId)).willReturn(Arrays.asList());

        MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
                
        verify(reviewService).getReviewsByEventId(eventId);
    }

    @Test
    void testGetReviewsByEventId_InternalServerError() throws Exception {
        given(reviewService.getReviewsByEventId(eventId))
                .willThrow(new RuntimeException("Database connection failed"));

        MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateReview() throws Exception {
        CreateReviewRequest requestBody = new CreateReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Good event");

        given(reviewService.createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class)))
                .willReturn(sampleReviewDTO);

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(reviewId)))
                .andExpect(jsonPath("$.rating", is(4)));
                
        verify(reviewService).createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class));
    }
    
    @Test
    void testCreateReview_UserAlreadyReviewed_Conflict() throws Exception {
        CreateReviewRequest requestBody = new CreateReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Trying to review again");

        given(reviewService.createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class)))
                .willThrow(new IllegalStateException("User has already reviewed this event"));

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isConflict());
                
        verify(reviewService).createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class));
    }

    @Test
    void testCreateReview_MissingUserId() throws Exception {
        CreateReviewRequest requestBody = new CreateReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Good event");

        mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());
                
        verifyNoInteractions(reviewService);
    }

    @Test
    void testCreateReview_InternalServerError() throws Exception {
        CreateReviewRequest requestBody = new CreateReviewRequest();
        requestBody.setRating(4);
        requestBody.setComment("Good event");

        given(reviewService.createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class)))
                .willThrow(new RuntimeException("Database error"));

        MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isInternalServerError());
                
        verify(reviewService).createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class));
    }

    @Test
    void testUpdateReview() throws Exception {
        UpdateReviewRequest requestBody = new UpdateReviewRequest();
        requestBody.setRating(5);
        requestBody.setComment("Updated: Excellent event!");
        
        ReviewDTO updatedReviewDTO = ReviewDTO.builder()
                .id(reviewId).rating(5).comment("Updated: Excellent event!")
                .createdAt(sampleReviewDTO.getCreatedAt()).updatedAt(ZonedDateTime.now())
                .userId(userId).username("User 1").eventId(eventId).build();

        given(reviewService.updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class)))
                .willReturn(updatedReviewDTO);

        MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reviewId)))
                .andExpect(jsonPath("$.rating", is(5)));
                
        verify(reviewService).updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class));
    }
    
    @Test
    void testUpdateReview_NotFound() throws Exception {
        UpdateReviewRequest requestBody = new UpdateReviewRequest();
        requestBody.setRating(5);
        String nonExistentReviewId = "nonexistent_rev_id";

        given(reviewService.updateReview(eq(userId), eq(nonExistentReviewId), any(UpdateReviewRequest.class)))
                .willThrow(new NotFoundException("Review not found"));

        MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
                
        verify(reviewService).updateReview(eq(userId), eq(nonExistentReviewId), any(UpdateReviewRequest.class));
    }

    @Test
    void testUpdateReview_Unauthorized() throws Exception {
        UpdateReviewRequest requestBody = new UpdateReviewRequest();
        String differentUserId = "usr_789";

        given(reviewService.updateReview(eq(differentUserId), eq(reviewId), any(UpdateReviewRequest.class)))
                .willThrow(new UnauthorizedException("Not authorized"));

        MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody))
                        .header("X-User-ID", differentUserId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isForbidden());
                
        verify(reviewService).updateReview(eq(differentUserId), eq(reviewId), any(UpdateReviewRequest.class));
    }

    @Test
    void testDeleteReview() throws Exception {
        doNothing().when(reviewService).deleteReview(userId, reviewId);

        MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNoContent());
                
        verify(reviewService).deleteReview(userId, reviewId);
    }

    @Test
    void testDeleteReview_NotFound() throws Exception {
        String nonExistentReviewId = "nonexistent_rev_id";
        doThrow(new NotFoundException("Review not found"))
                .when(reviewService).deleteReview(userId, nonExistentReviewId);

        MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
                        .header("X-User-ID", userId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isNotFound());
                
        verify(reviewService).deleteReview(userId, nonExistentReviewId);
    }

    @Test
    void testDeleteReview_Unauthorized() throws Exception {
        String differentUserId = "usr_789";
        doThrow(new UnauthorizedException("Not authorized"))
                .when(reviewService).deleteReview(differentUserId, reviewId);

        MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
                        .header("X-User-ID", differentUserId))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isForbidden());
                
        verify(reviewService).deleteReview(differentUserId, reviewId);
    }
}