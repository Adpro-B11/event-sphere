package id.ac.ui.cs.advprog.eventsphere.event.command;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class UpdateEventInfoCommandTest {
    private EventService eventService;
    private UpdateEventInfoCommand command;
    private String eventId;
    private Event updated;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        eventId = "123";
        updated = new Event();
        command = new UpdateEventInfoCommand(eventService, eventId, updated);
    }

    @Test
    void testExecute() {
        command.execute();
        verify(eventService, times(1)).updateEventInfo(eventId, updated);
    }

}