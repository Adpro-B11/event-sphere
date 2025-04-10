// src/test/java/model/EventTest.java
package id.ac.ui.cs.advprog.eventsphere.event.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;

    @BeforeEach
    void setUp() {
        event = new Event();
    }

    @Test
    void testCreateEvent_Success() {
        // Arrange & Act
        event.setTitle("Konser Musik");
        event.setDescription("Konser musik tahunan");
        event.setDate("2023-12-01");
        event.setLocation("Jakarta");
        event.setPrice(500000.0);

        // Assert
        assertEquals("Konser Musik", event.getTitle());
        assertEquals("Konser musik tahunan", event.getDescription());
        assertEquals("2023-12-01", event.getDate());
        assertEquals("Jakarta", event.getLocation());
        assertEquals(500000.0, event.getPrice());
        assertEquals("DRAFT", event.getStatus()); // Default status
    }

    @Test
    void testCreateEvent_InvalidTitle() {
        // Arrange & Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            event.setTitle("");
        });

        // Assert
        assertEquals("Title cannot be empty", exception.getMessage());
    }

    @Test
    void testSetStatus_ValidStatus() {
        // Arrange & Act
        event.setStatus("PUBLISHED");

        // Assert
        assertEquals("PUBLISHED", event.getStatus());
    }

    @Test
    void testSetStatus_InvalidStatus() {
        // Arrange & Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            event.setStatus("INVALID_STATUS");
        });

        // Assert
        assertEquals("Invalid status", exception.getMessage());
    }
}