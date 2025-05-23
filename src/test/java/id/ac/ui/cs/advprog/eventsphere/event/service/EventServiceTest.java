package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    private final String eventId = "123";

    @BeforeEach
    void setUp() {
        reset(eventRepository);
    }

    @Test
    void testCreateEvent_Success() {
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        eventService.createEvent(event);

        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testUpdateStatus_Success() {
        Event e = new Event();
        e.setId(eventId);
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(e));

        eventService.updateStatus(eventId, EventStatus.PUBLISHED.getValue());

        assertThat(e.getStatus())
                .isEqualTo(EventStatus.PUBLISHED.getValue());
        verify(eventRepository).save(e);
    }

    @Test
    void testUpdateStatus_InvalidStatus() {
        Event e = new Event();
        e.setId(eventId);
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(e));

        assertThatThrownBy(() ->
                eventService.updateStatus(eventId, "INVALID_STATUS")
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testFindById_ValidId() {
        Event e = new Event();
        e.setId(eventId);
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(e));

        Event result = eventService.findById(eventId);
        assertThat(result).isNotNull();
    }

    @Test
    void testFindById_InvalidId() {
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.findById(eventId)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testFindByOrganizer_Valid() {
        String org = "Mas Inis";
        Event e = new Event();
        e.setOrganizer(org);
        when(eventRepository.findByOrganizer(org))
                .thenReturn(List.of(e));

        List<Event> list = eventService.findAllByOrganizer(org);
        assertThat(list).hasSize(1).contains(e);
    }

    @Test
    void testFindByOrganizer_Invalid() {
        String org = "Unknown";
        when(eventRepository.findByOrganizer(org))
                .thenReturn(List.of());

        List<Event> list = eventService.findAllByOrganizer(org);
        assertThat(list).isEmpty();
    }

    @Test
    void testUpdateEventInfo_Success() {
        Event existing = new Event();
        existing.setId(eventId);
        existing.setDate("2025-06-20");
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.of(existing));

        Event updated = new Event();
        updated.setTitle("New");
        updated.setDescription("NewDesc");
        updated.setDate("2025-06-30");
        updated.setLocation("NewLoc");
        updated.setPrice(200.0);

        eventService.updateEventInfo(eventId, updated);

        assertThat(existing.getTitle()).isEqualTo("New Title");
        assertThat(existing.getDate()).isEqualTo("2025-06-30");
        verify(eventRepository).save(existing);
    }

    @Test
    void testUpdateEventInfo_InvalidId() {
        when(eventRepository.findById(eventId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.updateEventInfo(eventId, new Event())
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testDeleteEvent_Success() {
        when(eventRepository.existsById(eventId))
                .thenReturn(true);

        eventService.deleteEvent(eventId);

        verify(eventRepository).deleteById(eventId);
    }

    @Test
    void testDeleteEvent_NotFound() {
        when(eventRepository.existsById(eventId))
                .thenReturn(false);

        assertThatThrownBy(() ->
                eventService.deleteEvent(eventId)
        ).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testFindAllEvents() {
        Event a = new Event(), b = new Event();
        when(eventRepository.findAll())
                .thenReturn(List.of(a, b));

        List<Event> all = eventService.findAllEvents();
        assertThat(all).hasSize(2);
    }
}