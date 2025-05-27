package id.ac.ui.cs.advprog.eventsphere.ticket.factory;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TicketFactoryTest {

    private TicketFactory ticketFactory;

    @BeforeEach
    void setUp() {
        ticketFactory = new TicketFactory();
    }

    @Test
    void testCreateRegularTicket() {
        Ticket ticket = ticketFactory.createTicket("event123", TicketType.REGULAR, 100.0, 50);

        assertEquals("event123", ticket.getEventId());
        assertEquals(TicketType.REGULAR, ticket.getType());
        assertEquals(100.0, ticket.getPrice());
        assertEquals(50, ticket.getQuota());
        assertEquals(50, ticket.getRemaining());
    }

    @Test
    void testCreateEarlyBirdTicketWithValidQuota() {
        Ticket ticket = ticketFactory.createTicket("event123", TicketType.EARLY_BIRD, 80.0, 90);

        assertEquals(90, ticket.getQuota());
        assertEquals(90, ticket.getRemaining());
    }

    @Test
    void testCreateEarlyBirdTicketWithExcessiveQuota() {
        Ticket ticket = ticketFactory.createTicket("event123", TicketType.EARLY_BIRD, 80.0, 150);

        assertEquals(100, ticket.getQuota(), "Quota should be capped at 100");
        assertEquals(100, ticket.getRemaining(), "Remaining should match capped quota");
    }

    @Test
    void testCreateStudentTicketAppliesDiscount() {
        Ticket ticket = ticketFactory.createTicket("event123", TicketType.STUDENT, 100.0, 30);

        assertEquals(100, ticket.getPrice());
    }

    @Test
    void testCreateGroupTicket_NoSpecialLogicYet() {
        Ticket ticket = ticketFactory.createTicket("event123", TicketType.GROUP, 300.0, 10);

        assertEquals(300, ticket.getPrice());
        assertEquals(10, ticket.getQuota());
    }
}