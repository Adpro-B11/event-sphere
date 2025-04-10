package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void testCreateEvent_Success() {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        // Act
        eventService.createEvent(event);

        // Assert
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testUpdateStatus_Success() {
        // Arrange
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(event);

        // Act
        eventService.updateStatus(eventId, EventStatus.PUBLISHED.getValue());

        // Assert
        assertEquals(EventStatus.PUBLISHED.getValue(), event.getStatus());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testUpdateStatus_InvalidStatus() {
        // Arrange
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(event);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            eventService.updateStatus(eventId, "INVALID_STATUS");
        });
    }

    @Test
    void testFindById_ValidId() {
        // Arrange
        String eventId = "123";
        Event event = new Event();
        event.setId(eventId);
        when(eventRepository.findById(eventId)).thenReturn(event);

        // Act & Assert
        assertNotNull(eventService.findById(eventId));
    }

    @Test
    void testFindById_InvalidId() {
        // Arrange
        String eventId = "INVALID_ID";
        when(eventRepository.findById(eventId)).thenReturn(null);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            eventService.findById(eventId);
        });
    }

    @Test
    void testFindAllByOrganizer_ValidOrganizer() {
        // Arrange
        String organizer = "Mas Inis";
        Event event = new Event();
        event.setOrganizer(organizer);
        when(eventRepository.findAllByOrganizer(organizer)).thenReturn(List.of(event));

        // Act
        var events = eventService.findAllByOrganizer(organizer);

        // Assert
        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }

    @Test
    void testFindAllByOrganizer_InvalidOrganizer() {
        // Arrange
        String organizer = "mas inis"; // Lowercase to test case sensitivity
        when(eventRepository.findAllByOrganizer(organizer)).thenReturn(List.of());

        // Act
        var events = eventService.findAllByOrganizer(organizer);

        // Assert
        assertTrue(events.isEmpty());
    }
}