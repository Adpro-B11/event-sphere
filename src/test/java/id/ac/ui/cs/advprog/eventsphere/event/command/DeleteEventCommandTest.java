package id.ac.ui.cs.advprog.eventsphere.event.command;

import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class DeleteEventCommandTest {
    private EventService eventService;
    private DeleteEventCommand command;
    private String eventId;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
        eventId = "123";
        command = new DeleteEventCommand(eventService, eventId);
    }

    @Test
    void testExecute() {
        command.execute();
        verify(eventService, times(1)).deleteEvent(eventId);
    }
}