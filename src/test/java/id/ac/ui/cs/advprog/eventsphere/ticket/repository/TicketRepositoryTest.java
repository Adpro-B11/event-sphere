package id.ac.ui.cs.advprog.eventsphere.ticket.repository;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketRepositoryTest {

    private TicketRepository repository;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        repository = new TicketRepository();
        ticket = new Ticket();
        ticket.setEventId("event123");
        ticket.setType(TicketType.REGULAR);
        ticket.setPrice(100.0);
        ticket.setQuota(50);
        ticket.setRemaining(50);
        ticket.setActive(true);
    }

    @Test
    public void testSaveAndFindById() {
        repository.save(ticket);
        Ticket found = repository.findById(ticket.getId());
        assertNotNull(found);
        assertEquals(ticket.getId(), found.getId());
    }

    @Test
    public void testFindByIdNotFoundReturnsNull() {
        Ticket result = repository.findById("nonexistent-id");
        assertNull(result);
    }

    @Test
    public void testFindByEventIdOnlyReturnsActiveTickets() {
        Ticket inactiveTicket = new Ticket();
        inactiveTicket.setEventId("event123");
        inactiveTicket.setType(TicketType.VIP);
        inactiveTicket.setActive(false);

        repository.save(ticket);
        repository.save(inactiveTicket);

        List<Ticket> result = repository.findByEventId("event123");
        assertEquals(1, result.size());
        assertEquals(ticket.getId(), result.get(0).getId());
    }

    @Test
    public void testUpdateTicket() {
        repository.save(ticket);

        ticket.setPrice(200.0);
        repository.update(ticket);

        Ticket updated = repository.findById(ticket.getId());
        assertEquals(200.0, updated.getPrice());
    }

    @Test
    public void testUpdateNonexistentTicketDoesNothing() {
        Ticket newTicket = new Ticket();
        newTicket.setEventId("eventX");
        newTicket.setType(TicketType.VIP);
        newTicket.setPrice(300.0);

        repository.update(newTicket);
        Ticket shouldBeNull = repository.findById(newTicket.getId());
        assertNull(shouldBeNull);
    }

    @Test
    public void testDeleteTicketSoftDeletes() {
        repository.save(ticket);
        repository.delete(ticket.getId());

        Ticket deleted = repository.findById(ticket.getId());
        assertFalse(deleted.isActive());

        List<Ticket> eventTickets = repository.findByEventId(ticket.getEventId());
        assertTrue(eventTickets.isEmpty()); // deleted one should not be returned
    }

    @Test
    public void testFindAllReturnsAllTickets() {
        Ticket another = new Ticket();
        another.setEventId("eventABC");
        another.setType(TicketType.VIP);
        another.setPrice(250.0);
        another.setQuota(100);
        another.setRemaining(90);

        repository.save(ticket);
        repository.save(another);

        List<Ticket> all = repository.findAll();
        assertEquals(2, all.size());
    }
}
