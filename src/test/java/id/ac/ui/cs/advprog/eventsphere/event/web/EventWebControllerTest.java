package id.ac.ui.cs.advprog.eventsphere.event.web;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventWebController.class)
class EventWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void listEvents_ShouldReturnViewWithModel() throws Exception {
        Event e1 = new Event(); e1.setId("1"); e1.setTitle("A1");
        Event e2 = new Event(); e2.setId("2"); e2.setTitle("A2");
        given(eventService.findAllEvents()).willReturn(List.of(e1, e2));

        mockMvc.perform(get("/ui/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attribute("events", List.of(e1, e2)));
    }

    @Test
    void showCreateForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/ui/events/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("event-form"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    void saveEvent_ShouldRedirectAndCallService() throws Exception {
        willDoNothing().given(eventService).createEvent(any(Event.class));

        mockMvc.perform(post("/ui/events")
                        .param("title", "T")
                        .param("description", "D")
                        .param("date", "2025-06-01")
                        .param("location", "L")
                        .param("price", "50.0")
                        .param("organizer", "O"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ui/events"));

        then(eventService).should().createEvent(any(Event.class));
    }

    @Test
    void showEditForm_ShouldPopulateModel() throws Exception {
        String id = "123";
        Event evt = new Event(); evt.setId(id); evt.setTitle("Old");
        given(eventService.findById(id)).willReturn(evt);

        mockMvc.perform(get("/ui/events/{id}/edit", id))
                .andExpect(status().isOk())
                .andExpect(view().name("event-form"))
                .andExpect(model().attribute("event", evt));
    }

    @Test
    void submitEdit_ShouldRedirectAndCallUpdate() throws Exception {
        String id = "123";
        willDoNothing().given(eventService).updateEventInfo(eq(id), any(Event.class));

        mockMvc.perform(post("/ui/events/{id}/edit", id)
                        .param("id", id)
                        .param("title", "New")
                        .param("description", "ND")
                        .param("date", "2025-07-01")
                        .param("location", "L2")
                        .param("price", "75.0")
                        .param("organizer", "O2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ui/events"));

        then(eventService).should().updateEventInfo(eq(id), any(Event.class));
    }

    @Test
    void deleteEvent_ShouldRedirectAndCallDelete() throws Exception {
        String id = "123";
        willDoNothing().given(eventService).deleteEvent(id);

        mockMvc.perform(get("/ui/events/{id}/delete", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ui/events"));

        then(eventService).should().deleteEvent(id);
    }
}
