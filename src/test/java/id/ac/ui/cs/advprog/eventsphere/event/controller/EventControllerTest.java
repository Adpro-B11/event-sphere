package id.ac.ui.cs.advprog.eventsphere.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateEvent() throws Exception {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser");
        event.setDescription("desc");
        event.setDate("2025-06-01");
        event.setLocation("Jakarta");
        event.setPrice(100.0);
        event.setOrganizer("Org");

        willDoNothing().given(eventService).createEvent(any(Event.class));

        // Act & Assert
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", matchesPattern(".*/events/.+")));

        then(eventService).should().createEvent(any(Event.class));
    }

    @Test
    void testGetEventById_Success() throws Exception {
        // Arrange
        String id = "123";
        Event event = new Event();
        event.setId(id);
        event.setTitle("Konser");
        given(eventService.findById(id)).willReturn(event);

        // Act & Assert
        mockMvc.perform(get("/events/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Konser"));
    }

    @Test
    void testGetEventById_NotFound() throws Exception {
        // Arrange
        String id = "notfound";
        given(eventService.findById(id)).willThrow(new NoSuchElementException());

        // Act & Assert
        mockMvc.perform(get("/events/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllEvents() throws Exception {
        // Arrange
        Event e1 = new Event(); e1.setId("1"); e1.setTitle("A1");
        Event e2 = new Event(); e2.setId("2"); e2.setTitle("A2");
        given(eventService.findAllEvents()).willReturn(List.of(e1, e2));

        // Act & Assert
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void testUpdateEventInfo() throws Exception {
        // Arrange
        String id = "123";
        Event updated = new Event();
        updated.setTitle("New"); updated.setDescription("NewDesc");
        updated.setDate("2025-07-01"); updated.setLocation("L");
        updated.setPrice(200.0);
        willDoNothing().given(eventService).updateEventInfo(eq(id), any(Event.class));

        // Act & Assert
        mockMvc.perform(put("/events/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        then(eventService).should().updateEventInfo(eq(id), any(Event.class));
    }

    @Test
    void testUpdateStatus() throws Exception {
        // Arrange
        String id = "123";
        String body = "{\"status\":\"PUBLISHED\"}";
        willDoNothing().given(eventService).updateStatus(eq(id), eq("PUBLISHED"));

        // Act & Assert
        mockMvc.perform(patch("/events/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        then(eventService).should().updateStatus(id, "PUBLISHED");
    }

    @Test
    void testDeleteEvent() throws Exception {
        // Arrange
        String id = "123";
        willDoNothing().given(eventService).deleteEvent(id);

        // Act & Assert
        mockMvc.perform(delete("/events/{id}", id))
                .andExpect(status().isNoContent());

        then(eventService).should().deleteEvent(id);
    }
}
