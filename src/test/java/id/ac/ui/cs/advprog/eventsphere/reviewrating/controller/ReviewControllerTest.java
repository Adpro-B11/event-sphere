package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.NotFoundException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.exception.UnauthorizedException;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetReviewsByEventId() throws Exception {
        // Arrange
        String eventId = "evt_123";
        ReviewDTO review1 = ReviewDTO.builder()
                .id("rev_123")
                .rating(4)
                .comment("Good event")
                .createdAt(ZonedDateTime.now())
                .userId("usr_123")
                .username("User 1")
                .eventId(eventId)
                .build();

        ReviewDTO review2 = ReviewDTO.builder()
                .id("rev_456")
                .rating(5)
                .comment("Excellent!")
                .createdAt(ZonedDateTime.now())
                .userId("usr_456")
                .username("User 2")
                .eventId(eventId)
                .build();

        List<ReviewDTO> reviews = Arrays.asList(review1, review2);

        given(reviewService.getReviewsByEventId(eventId)).willReturn(reviews);

        // Act & Assert
        mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("rev_123")))
                .andExpect(jsonPath("$[0].rating", is(4)))
                .andExpect(jsonPath("$[0].comment", is("Good event")))
                .andExpect(jsonPath("$[0].userId", is("usr_123")))
                .andExpect(jsonPath("$[0].username", is("User 1")))
                .andExpect(jsonPath("$[1].id", is("rev_456")))
                .andExpect(jsonPath("$[1].rating", is(5)));
    }

    @Test
    void testCreateReview() throws Exception {
        // Arrange
        String eventId = "evt_123";
        String userId = "usr_123";

        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);
        request.setComment("Good event");

        ReviewDTO createdReview = ReviewDTO.builder()
                .id("rev_123")
                .rating(4)
                .comment("Good event")
                .createdAt(ZonedDateTime.now())
                .userId(userId)
                .username("User 1")
                .eventId(eventId)
                .build();

        given(reviewService.createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class)))
                .willReturn(createdReview);

        // Act & Assert
        mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-ID", userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("rev_123")))
                .andExpect(jsonPath("$.rating", is(4)))
                .andExpect(jsonPath("$.comment", is("Good event")))
                .andExpect(jsonPath("$.userId", is(userId)));

        verify(reviewService).createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class));
    }

    @Test
    void testCreateReview_MissingUserId() throws Exception {
        // Arrange
        String eventId = "evt_123";

        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);
        request.setComment("Good event");

        // Act & Assert
        mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateReview() throws Exception {
        // Arrange
        String reviewId = "rev_123";
        String userId = "usr_123";

        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated: Excellent event!");

        ReviewDTO updatedReview = ReviewDTO.builder()
                .id(reviewId)
                .rating(5)
                .comment("Updated: Excellent event!")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .userId(userId)
                .username("User 1")
                .eventId("evt_123")
                .build();

        given(reviewService.updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class)))
                .willReturn(updatedReview);

        // Act & Assert
        mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", "evt_123", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-ID", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(reviewId)))
                .andExpect(jsonPath("$.rating", is(5)))
                .andExpect(jsonPath("$.comment", is("Updated: Excellent event!")))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));

        verify(reviewService).updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class));
    }

    @Test
    void testUpdateReview_NotFound() throws Exception {
        // Arrange
        String reviewId = "nonexistent";
        String userId = "usr_123";

        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");

        given(reviewService.updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class)))
                .willThrow(new NotFoundException("Review not found"));

        // Act & Assert
        mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", "evt_123", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-ID", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateReview_Unauthorized() throws Exception {
        // Arrange
        String reviewId = "rev_123";
        String userId = "usr_456"; // Different from the review owner

        UpdateReviewRequest request = new UpdateReviewRequest();
        request.setRating(5);
        request.setComment("Updated comment");

        given(reviewService.updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class)))
                .willThrow(new UnauthorizedException("Not authorized to update this review"));

        // Act & Assert
        mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", "evt_123", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("X-User-ID", userId))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteReview() throws Exception {
        // Arrange
        String reviewId = "rev_123";
        String userId = "usr_123";

        // Act & Assert
        mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", "evt_123", reviewId)
                        .header("X-User-ID", userId))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(userId, reviewId);
    }

    @Test
    void testDeleteReview_NotFound() throws Exception {
        // Arrange
        String reviewId = "nonexistent";
        String userId = "usr_123";

        doThrow(new NotFoundException("Review not found"))
                .when(reviewService).deleteReview(userId, reviewId);

        // Act & Assert
        mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", "evt_123", reviewId)
                        .header("X-User-ID", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteReview_Unauthorized() throws Exception {
        // Arrange
        String reviewId = "rev_123";
        String userId = "usr_456"; // Different from the review owner

        doThrow(new UnauthorizedException("Not authorized to delete this review"))
                .when(reviewService).deleteReview(userId, reviewId);

        // Act & Assert
        mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", "evt_123", reviewId)
                        .header("X-User-ID", userId))
                .andExpect(status().isForbidden());
    }
}