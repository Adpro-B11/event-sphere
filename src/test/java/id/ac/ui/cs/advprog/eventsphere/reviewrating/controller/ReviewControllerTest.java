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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @MockBean 
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    @Autowired
    private ObjectMapper objectMapper;

    private String eventId = "evt_123";
    private String userId = "usr_123";
    private String reviewId = "rev_123";
    private ReviewDTO sampleReviewDTO;
    private List<ReviewDTO> reviewList;

    @BeforeEach
    void setUp() {
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

//     @Test
//     void testGetReviewsByEventId() throws Exception {
//         given(reviewService.getReviewsByEventId(eventId)).willReturn(reviewList);

//         MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)))
//                 .andExpect(jsonPath("$[0].id", is(reviewId)));
//     }

//     @Test
//     void testCreateReview() throws Exception {
//         CreateReviewRequest requestBody = new CreateReviewRequest();
//         requestBody.setRating(4);
//         requestBody.setComment("Good event");

//         given(reviewService.createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class)))
//                 .willReturn(sampleReviewDTO);

//         MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody))
//                         .header("X-User-ID", userId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();
        
//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id", is(reviewId)));
//         verify(reviewService).createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class));
//     }
    
//     @Test
//     void testCreateReview_UserAlreadyReviewed_Conflict() throws Exception {
//         CreateReviewRequest requestBody = new CreateReviewRequest();
//         requestBody.setRating(4);
//         requestBody.setComment("Trying to review again");

//         given(reviewService.createReview(eq(userId), eq(eventId), any(CreateReviewRequest.class)))
//                 .willThrow(new IllegalStateException("User has already reviewed this event"));

//         MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody))
//                         .header("X-User-ID", userId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();
        
//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isConflict()); 
//     }

//     @Test
//     void testCreateReview_MissingUserId() throws Exception {
//         CreateReviewRequest requestBody = new CreateReviewRequest();
//         requestBody.setRating(4);
//         requestBody.setComment("Good event");

//         mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(status().isBadRequest());
//     }

//     @Test
//     void testUpdateReview() throws Exception {
//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         requestBody.setRating(5);
//         requestBody.setComment("Updated: Excellent event!");
//         ReviewDTO updatedReviewDTO = ReviewDTO.builder()
//                 .id(reviewId).rating(5).comment("Updated: Excellent event!")
//                 .createdAt(sampleReviewDTO.getCreatedAt()).updatedAt(ZonedDateTime.now())
//                 .userId(userId).username("User 1").eventId(eventId).build();

//         given(reviewService.updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class)))
//                 .willReturn(updatedReviewDTO);

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody))
//                         .header("X-User-ID", userId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id", is(reviewId)))
//                 .andExpect(jsonPath("$.rating", is(5)));
//         verify(reviewService).updateReview(eq(userId), eq(reviewId), any(UpdateReviewRequest.class));
//     }
    
//     @Test
//     void testUpdateReview_NotFound() throws Exception {
//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         requestBody.setRating(5);
//         String nonExistentReviewId = "nonexistent_rev_id";

//         given(reviewService.updateReview(eq(userId), eq(nonExistentReviewId), any(UpdateReviewRequest.class)))
//                 .willThrow(new NotFoundException("Review not found"));

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody))
//                         .header("X-User-ID", userId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();
        
//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     void testUpdateReview_Unauthorized() throws Exception {
//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         String differentUserId = "usr_789";

//         given(reviewService.updateReview(eq(differentUserId), eq(reviewId), any(UpdateReviewRequest.class)))
//                 .willThrow(new UnauthorizedException("Not authorized"));

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody))
//                         .header("X-User-ID", differentUserId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();
        
//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isForbidden());
//     }

//     @Test
//     void testDeleteReview() throws Exception {
//         doNothing().when(reviewService).deleteReview(userId, reviewId);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .header("X-User-ID", userId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isNoContent());
//         verify(reviewService).deleteReview(userId, reviewId);
//     }

//     @Test
//     void testDeleteReview_NotFound() throws Exception {
//         String nonExistentReviewId = "nonexistent_rev_id";
//         doThrow(new NotFoundException("Review not found"))
//                 .when(reviewService).deleteReview(userId, nonExistentReviewId);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
//                         .header("X-User-ID", userId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();
        
//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     void testDeleteReview_Unauthorized() throws Exception {
//         String differentUserId = "usr_789";
//         doThrow(new UnauthorizedException("Not authorized"))
//                 .when(reviewService).deleteReview(differentUserId, reviewId);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .header("X-User-ID", differentUserId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();
        
//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isForbidden());
//     }
}