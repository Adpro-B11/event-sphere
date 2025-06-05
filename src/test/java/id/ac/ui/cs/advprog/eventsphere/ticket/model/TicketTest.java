package id.ac.ui.cs.advprog.eventsphere.ticket.model;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        ticket = new Ticket();
    }

    @Test
    public void testSetAndGetValidValues() {
        ticket.setEventId("event-abc");
        ticket.setType(TicketType.VIP);
        ticket.setPrice(150.0);
        ticket.setQuota(100);
        ticket.setRemaining(50);
        ticket.setActive(false);

        assertEquals("event-abc", ticket.getEventId());
        assertEquals(TicketType.VIP, ticket.getType());
        assertEquals(150.0, ticket.getPrice());
        assertEquals(100, ticket.getQuota());
        assertEquals(50, ticket.getRemaining());
        assertFalse(ticket.isActive());
    }

    @Test
    public void testTicketIdIsGeneratedAutomatically() {
        assertNotNull(ticket.getId());
        assertTrue(ticket.getId().length() > 0);
    }

    @Test
    public void testIsActiveDefaultsToTrue() {
        assertTrue(ticket.isActive());
    }

    @Test
    public void testSetNegativePriceThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticket.setPrice(-10)
        );
        assertEquals("Ticket price cannot be negative", exception.getMessage());
    }

    @Test
    public void testSetNegativeQuotaThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticket.setQuota(-5)
        );
        assertEquals("Quota cannot be negative", exception.getMessage());
    }

    @Test
    public void testSetInvalidRemainingGreaterThanQuotaThrowsException() {
        ticket.setQuota(100);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticket.setRemaining(150)
        );
        assertEquals("Remaining tickets must be between 0 and quota", exception.getMessage());
    }

    @Test
    public void testSetInvalidRemainingLessThanZeroThrowsException() {
        ticket.setQuota(100);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticket.setRemaining(-1)
        );
        assertEquals("Remaining tickets must be between 0 and quota", exception.getMessage());
    }

    @Test
    public void testSetNullTypeThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> ticket.setType(null)
        );
        assertEquals("Ticket type cannot be null", exception.getMessage());
    }
}
