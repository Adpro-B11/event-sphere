package id.ac.ui.cs.advprog.eventsphere.event.command;

import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class EventCommandTest {

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);
    }

    @Test
    void testCreateEventCommand_Execute() {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser Musik");
        CreateEventCommand command = new CreateEventCommand(eventService, event);

        // Act
        command.execute();

        // Assert
        verify(eventService, times(1)).createEvent(event);
    }

    @Test
    void testUpdateStatusCommand_Execute() {
        // Arrange
        String eventId = "123";
        String status = EventStatus.PUBLISHED.getValue();
        UpdateStatusCommand command = new UpdateStatusCommand(eventService, eventId, status);

        // Act
        command.execute();

        // Assert
        verify(eventService, times(1)).updateStatus(eventId, status);
    }
}