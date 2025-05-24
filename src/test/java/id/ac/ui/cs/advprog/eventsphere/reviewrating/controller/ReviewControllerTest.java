package id.ac.ui.cs.advprog.eventsphere.reviewrating.controller;

import id.ac.ui.cs.advprog.eventsphere.auth.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.auth.resolver.CurrentUserResolver;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.CreateReviewRequest;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.ReviewDTO;
import id.ac.ui.cs.advprog.eventsphere.reviewrating.dto.UpdateReviewRequest;
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
import java.util.UUID;

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

    @Mock
    private CurrentUserResolver currentUserResolver;

    @InjectMocks
    private ReviewController reviewController;

    private ObjectMapper objectMapper;

    private String eventId = "evt_123";
    private UUID userId = UUID.fromString("51de080c-59d7-4e70-bcb8-42986a84902f");
    private String reviewId = "rev_123";
    private User mockUser;
    private ReviewDTO sampleReviewDTO;
    private List<ReviewDTO> reviewList;

    @BeforeEach
    void setUp() {
        // Setup MockMvc with CurrentUserResolver
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                .setCustomArgumentResolvers(currentUserResolver)
                .setAsyncRequestTimeout(5000)
                .build();
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Create mock User object
        mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setRole(Role.USER);

        // Setup CurrentUserResolver mock
        when(currentUserResolver.supportsParameter(any())).thenReturn(true);
        when(currentUserResolver.resolveArgument(any(), any(), any(), any())).thenReturn(mockUser);

        sampleReviewDTO = ReviewDTO.builder()
                .id(reviewId)
                .rating(4)
                .comment("Good event")
                .createdAt(ZonedDateTime.now())
                .userId(userId.toString())
                .username("testuser")
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
//                 .andExpect(jsonPath("$[0].id", is(reviewId)))
//                 .andExpect(jsonPath("$[0].rating", is(4)))
//                 .andExpect(jsonPath("$[0].username", is("testuser")));
                
//         verify(reviewService).getReviewsByEventId(eventId);
//     }

//     @Test
//     void testGetReviewsByEventId_EmptyList() throws Exception {
//         given(reviewService.getReviewsByEventId(eventId)).willReturn(Arrays.asList());

//         MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/reviews", eventId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(0)));
                
//         verify(reviewService).getReviewsByEventId(eventId);
//     }

//     @Test
//     void testCreateReview_WithValidUser() throws Exception {
//         CreateReviewRequest requestBody = new CreateReviewRequest();
//         requestBody.setRating(4);
//         requestBody.setComment("Good event");

//         given(reviewService.createReview(eq(userId.toString()), eq(eventId), any(CreateReviewRequest.class)))
//                 .willReturn(sampleReviewDTO);

//         MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.id", is(reviewId)))
//                 .andExpect(jsonPath("$.rating", is(4)))
//                 .andExpect(jsonPath("$.comment", is("Good event")));
                
//         verify(reviewService).createReview(eq(userId.toString()), eq(eventId), any(CreateReviewRequest.class));
//     }

//     @Test
//     void testCreateReview_WithNullUser() throws Exception {
//         // Mock CurrentUserResolver to return null (unauthenticated user)
//         when(currentUserResolver.resolveArgument(any(), any(), any(), any())).thenReturn(null);

//         CreateReviewRequest requestBody = new CreateReviewRequest();
//         requestBody.setRating(4);
//         requestBody.setComment("Good event");

//         MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isUnauthorized());
                
//         verifyNoInteractions(reviewService);
//     }
    
//     @Test
//     void testCreateReview_UserAlreadyReviewed_Conflict() throws Exception {
//         CreateReviewRequest requestBody = new CreateReviewRequest();
//         requestBody.setRating(4);
//         requestBody.setComment("Trying to review again");

//         given(reviewService.createReview(eq(userId.toString()), eq(eventId), any(CreateReviewRequest.class)))
//                 .willThrow(new IllegalStateException("User has already reviewed this event"));

//         MvcResult mvcResult = mockMvc.perform(post("/api/events/{eventId}/reviews", eventId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isConflict());
                
//         verify(reviewService).createReview(eq(userId.toString()), eq(eventId), any(CreateReviewRequest.class));
//     }

//     @Test
//     void testUpdateReview_WithValidUser() throws Exception {
//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         requestBody.setRating(5);
//         requestBody.setComment("Updated: Excellent event!");
        
//         ReviewDTO updatedReviewDTO = ReviewDTO.builder()
//                 .id(reviewId).rating(5).comment("Updated: Excellent event!")
//                 .createdAt(sampleReviewDTO.getCreatedAt()).updatedAt(ZonedDateTime.now())
//                 .userId(userId.toString()).username("testuser").eventId(eventId).build();

//         given(reviewService.updateReview(eq(userId.toString()), eq(reviewId), any(UpdateReviewRequest.class)))
//                 .willReturn(updatedReviewDTO);

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id", is(reviewId)))
//                 .andExpect(jsonPath("$.rating", is(5)))
//                 .andExpect(jsonPath("$.comment", is("Updated: Excellent event!")));
                
//         verify(reviewService).updateReview(eq(userId.toString()), eq(reviewId), any(UpdateReviewRequest.class));
//     }

//     @Test
//     void testUpdateReview_WithNullUser() throws Exception {
//         // Mock CurrentUserResolver to return null (unauthenticated user)
//         when(currentUserResolver.resolveArgument(any(), any(), any(), any())).thenReturn(null);

//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         requestBody.setRating(5);

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isUnauthorized());
                
//         verifyNoInteractions(reviewService);
//     }
    
//     @Test
//     void testUpdateReview_NotFound() throws Exception {
//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         requestBody.setRating(5);
//         String nonExistentReviewId = "nonexistent_rev_id";

//         given(reviewService.updateReview(eq(userId.toString()), eq(nonExistentReviewId), any(UpdateReviewRequest.class)))
//                 .willThrow(new NotFoundException("Review not found"));

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isNotFound());
                
//         verify(reviewService).updateReview(eq(userId.toString()), eq(nonExistentReviewId), any(UpdateReviewRequest.class));
//     }

//     @Test
//     void testUpdateReview_Unauthorized() throws Exception {
//         UpdateReviewRequest requestBody = new UpdateReviewRequest();
//         requestBody.setRating(5);

//         given(reviewService.updateReview(eq(userId.toString()), eq(reviewId), any(UpdateReviewRequest.class)))
//                 .willThrow(new UnauthorizedException("Not authorized"));

//         MvcResult mvcResult = mockMvc.perform(put("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(requestBody)))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isForbidden());
                
//         verify(reviewService).updateReview(eq(userId.toString()), eq(reviewId), any(UpdateReviewRequest.class));
//     }

//     @Test
//     void testDeleteReview_WithValidUser() throws Exception {
//         doNothing().when(reviewService).deleteReview(userId.toString(), reviewId);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isNoContent());
                
//         verify(reviewService).deleteReview(userId.toString(), reviewId);
//     }

//     @Test
//     void testDeleteReview_WithNullUser() throws Exception {
//         // Mock CurrentUserResolver to return null (unauthenticated user)
//         when(currentUserResolver.resolveArgument(any(), any(), any(), any())).thenReturn(null);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isUnauthorized());
                
//         verifyNoInteractions(reviewService);
//     }

//     @Test
//     void testDeleteReview_NotFound() throws Exception {
//         String nonExistentReviewId = "nonexistent_rev_id";
//         doThrow(new NotFoundException("Review not found"))
//                 .when(reviewService).deleteReview(userId.toString(), nonExistentReviewId);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, nonExistentReviewId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isNotFound());
                
//         verify(reviewService).deleteReview(userId.toString(), nonExistentReviewId);
//     }

//     @Test
//     void testDeleteReview_Unauthorized() throws Exception {
//         doThrow(new UnauthorizedException("Not authorized"))
//                 .when(reviewService).deleteReview(userId.toString(), reviewId);

//         MvcResult mvcResult = mockMvc.perform(delete("/api/events/{eventId}/reviews/{reviewId}", eventId, reviewId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isForbidden());
                
//         verify(reviewService).deleteReview(userId.toString(), reviewId);
//     }

//     @Test
//     void testGetEventRatingSummary() throws Exception {
//         double expectedAverageRating = 4.2;
//         int expectedTotalReviews = 5;

//         given(ratingSummaryService.getAverageRating(eventId)).willReturn(expectedAverageRating);
//         given(ratingSummaryService.getTotalReviews(eventId)).willReturn(expectedTotalReviews);

//         MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/rating-summary", eventId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.eventId", is(eventId)))
//                 .andExpect(jsonPath("$.averageRating", is(expectedAverageRating)))
//                 .andExpect(jsonPath("$.totalReviews", is(expectedTotalReviews)));

//         verify(ratingSummaryService).getAverageRating(eventId);
//         verify(ratingSummaryService).getTotalReviews(eventId);
//     }

//     @Test
//     void testGetEventRatingSummary_ServiceThrowsException() throws Exception {
//         given(ratingSummaryService.getAverageRating(eventId))
//                 .willThrow(new RuntimeException("Database error"));

//         MvcResult mvcResult = mockMvc.perform(get("/api/events/{eventId}/rating-summary", eventId))
//                 .andExpect(request().asyncStarted())
//                 .andReturn();

//         mockMvc.perform(asyncDispatch(mvcResult))
//                 .andExpect(status().isInternalServerError());

//         verify(ratingSummaryService).getAverageRating(eventId);
//     }
}