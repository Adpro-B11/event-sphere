// src/test/java/model/EventTest.java
package id.ac.ui.cs.advprog.eventsphere.event.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;

class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    @Test
    void testCreateEvent_Success() {
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);
        event.setOrganizer("Mas Inis");

        assertEquals("Konser Musik", event.getTitle());
        assertEquals("Konser musik tahunan", event.getDescription());
        assertEquals("2023-12-01", event.getDate());
        assertEquals("Jakarta", event.getLocation());
        assertEquals(500000.0, event.getPrice());
        assertEquals(EventStatus.DRAFT.getValue(), event.getStatus()); // Default status
        assertEquals("Mas Inis", event.getOrganizer());
    }

    @Test
    void testCreateEvent_InvalidTitle() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            event.setTitle("");
        });

        assertEquals("Title cannot be empty", exception.getMessage());
    }

    @Test
    void testSetStatus_ValidStatus() {
        event.setStatus(EventStatus.PUBLISHED.getValue());

        assertEquals(EventStatus.PUBLISHED.getValue(), event.getStatus());
    }

    @Test
    void testSetStatus_InvalidStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            event.setStatus("INVALID_STATUS");
        });

        assertEquals("Invalid status", exception.getMessage());
    }
}