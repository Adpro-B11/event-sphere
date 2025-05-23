package id.ac.ui.cs.advprog.eventsphere.event.repository;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class EventRepositoryTest {

    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository = new EventRepository();
    }

    @Test
    void testSave_AddNewEvent() {
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        eventRepository.save(event);

        assertNotNull(eventRepository.findById(event.getId()));
    }

    @Test
    void testSave_UpdateExistingEvent() {
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        eventRepository.save(event);

        event.setDescription("Konser musik terbesar tahun ini");
        eventRepository.save(event);

        Event updatedEvent = eventRepository.findById(event.getId());
        assertEquals("Konser musik terbesar tahun ini", updatedEvent.getDescription());
    }

    @Test
    void testFindById_ValidId() {
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        eventRepository.save(event);

        assertNotNull(eventRepository.findById(event.getId()));
    }

    @Test
    void testFindById_InvalidId() {
        Event result = eventRepository.findById("INVALID_ID");

        assertNull(result);
    }

    @Test
    void testFindAllByOrganizer_ValidOrganizer() {
        Event event1 = new Event();
        event1.setTitle("Konser Musik");
        event1.setDescription("Konser musik tahunan");
        event1.setDate("2023-12-01");
        event1.setLocation("Jakarta");
        event1.setPrice(500000.0);
        event1.setStatus("DRAFT");
        event1.setOrganizer("Mas Inis");

        Event event2 = new Event();
        event2.setTitle("Seminar Teknologi");
        event2.setDescription("Seminar teknologi masa depan");
        event2.setDate("2023-11-15");
        event2.setLocation("Bandung");
        event2.setPrice(300000.0);
        event2.setStatus("PUBLISHED");
        event2.setOrganizer("Mbak Pao");

        eventRepository.save(event1);
        eventRepository.save(event2);

        List<Event> events = eventRepository.findAllByOrganizer("Mas Inis");

        assertFalse(events.isEmpty());
        assertEquals(1, events.size());
    }

    @Test
    void testFindAllByOrganizer_InvalidOrganizer() {
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setStatus("DRAFT");

        eventRepository.save(event);

        List<Event> events = eventRepository.findAllByOrganizer("Dek Atsini");

        assertTrue(events.isEmpty());
    }

    @Test
    void testDeleteById_Success() {
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Desc");
        event.setDate("2025-05-01");
        event.setLocation("Jakarta");
        event.setPrice(100.0);
        event.setOrganizer("Org");
        eventRepository.save(event);

        boolean removed = eventRepository.deleteById(event.getId());

        assertTrue(removed);
        assertNull(eventRepository.findById(event.getId()));
    }

    @Test
    void testDeleteById_NotFound() {
        boolean removed = eventRepository.deleteById("NOT_EXIST");
        assertFalse(removed);
    }

    @Test
    void testFindAll_ReturnsAll() {
        Event e1 = new Event();
        e1.setTitle("A1"); e1.setDescription("D1"); e1.setDate("2025-06-01");
        e1.setLocation("L1"); e1.setPrice(10.0); e1.setOrganizer("O1");
        Event e2 = new Event();
        e2.setTitle("A2"); e2.setDescription("D2"); e2.setDate("2025-07-01");
        e2.setLocation("L2"); e2.setPrice(20.0); e2.setOrganizer("O2");
        eventRepository.save(e1);
        eventRepository.save(e2);

        List<Event> all = eventRepository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(e1) && all.contains(e2));
    }
}
