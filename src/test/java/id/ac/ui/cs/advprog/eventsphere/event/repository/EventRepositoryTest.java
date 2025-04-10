package id.ac.ui.cs.advprog.eventsphere.event.repository;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventRepositoryTest {

    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository = new EventRepository();
    }

    @Test
    void testSave_AddNewEvent() {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);

        // Act
        eventRepository.save(event);

        // Assert
        assertNotNull(eventRepository.findById(event.getId()));
    }

    @Test
    void testSave_UpdateExistingEvent() {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);

        eventRepository.save(event);

        // Act
        event.setDescription("Konser musik terbesar tahun ini");
        eventRepository.save(event);

        // Assert
        Event updatedEvent = eventRepository.findById(event.getId());
        assertEquals("Konser musik terbesar tahun ini", updatedEvent.getDescription());
    }

    @Test
    void testFindById_ValidId() {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);

        eventRepository.save(event);

        // Act & Assert
        assertNotNull(eventRepository.findById(event.getId()));
    }

    @Test
    void testFindById_InvalidId() {
        // Arrange & Act
        Event result = eventRepository.findById("INVALID_ID");

        // Assert
        assertNull(result);
    }

    @Test
    void testFindAllByOrganizer_ValidOrganizer() {
        // Arrange
        Event event1 = new Event();
        event1.setTitle("Konser Musik");
        event1.setDescription("Konser musik tahunan");
        event1.setDate("2023-12-01");
        event1.setLocation("Jakarta");
        event1.setPrice(500000.0);
        event1.setStatus("DRAFT");

        Event event2 = new Event();
        event2.setTitle("Seminar Teknologi");
        event2.setDescription("Seminar teknologi masa depan");
        event2.setDate("2023-11-15");
        event2.setLocation("Bandung");
        event2.setPrice(300000.0);
        event2.setStatus("PUBLISHED");

        eventRepository.save(event1);
        eventRepository.save(event2);

        // Act
        var events = eventRepository.findAllByOrganizer("John Doe");

        // Assert
        assertFalse(events.isEmpty());
        assertEquals(2, events.size());
    }

    @Test
    void testFindAllByOrganizer_InvalidOrganizer() {
        // Arrange
        Event event = new Event();
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setStatus("DRAFT");

        eventRepository.save(event);

        // Act
        var events = eventRepository.findAllByOrganizer("john doe");

        // Assert
        assertTrue(events.isEmpty());
    }
}
