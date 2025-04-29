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
        List<Event> events = eventService.findAllByOrganizer(organizer);

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
        List<Event> events = eventService.findAllByOrganizer(organizer);

        // Assert
        assertTrue(events.isEmpty());
    }

    @Test
    void testUpdateEventInfo_Success() {
        String id = "123";
        Event existing = new Event();
        existing.setId(id);
        existing.setTitle("Old");
        existing.setDescription("OldDesc");
        existing.setDate("2025-04-30");
        existing.setLocation("Loc");
        existing.setPrice(100.0);
        when(eventRepository.findById(id)).thenReturn(existing);

        Event updated = new Event();
        updated.setTitle("New");
        updated.setDescription("NewDesc");
        updated.setDate("2025-05-01");
        updated.setLocation("NewLoc");
        updated.setPrice(200.0);

        eventService.updateEventInfo(id, updated);

        assertEquals("New", existing.getTitle());
        assertEquals("NewDesc", existing.getDescription());
        assertEquals("2025-05-01", existing.getDate());
        assertEquals("NewLoc", existing.getLocation());
        assertEquals(200.0, existing.getPrice());
        verify(eventRepository).save(existing);
    }

    @Test
    void testUpdateEventInfo_InvalidId() {
        String id = "NOT_FOUND";
        when(eventRepository.findById(id)).thenReturn(null);
        Event updated = new Event();
        assertThrows(NoSuchElementException.class, () -> eventService.updateEventInfo(id, updated));
    }

    @Test
    void testUpdateEventInfo_AfterDate() {
        String id = "123";
        Event past = new Event();
        past.setId(id);
        past.setDate("2025-04-28");
        when(eventRepository.findById(id)).thenReturn(past);
        Event updated = new Event();
        assertThrows(IllegalStateException.class, () -> eventService.updateEventInfo(id, updated));
    }

    @Test
    void testDeleteEvent_Success() {
        String id = "123";
        when(eventRepository.deleteById(id)).thenReturn(true);

        eventService.deleteEvent(id);

        verify(eventRepository).deleteById(id);
    }

    @Test
    void testDeleteEvent_NotFound() {
        when(eventRepository.deleteById("NOT_EXIST")).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> eventService.deleteEvent("NOT_EXIST"));
    }

    @Test
    void testFindAllEvents() {
        List<Event> list = List.of(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(list);

        List<Event> result = eventService.findAllEvents();

        assertEquals(2, result.size());
    }
}