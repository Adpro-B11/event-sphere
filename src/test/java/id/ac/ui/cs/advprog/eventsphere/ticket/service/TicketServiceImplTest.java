package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceImplTest {

    private TicketRepository repository;
    private TicketFactory factory;
    private TicketService service;

    @BeforeEach
    public void setUp() {
        repository = mock(TicketRepository.class);
        factory = mock(TicketFactory.class);
        service = new TicketServiceImpl(repository, factory);
    }

    @Test
    public void testCreateTicketExecutesCommandAndReturnsTicket() {
        Ticket ticket = new Ticket();
        when(factory.createTicket("event123", TicketType.VIP, 200.0, 50)).thenReturn(ticket);

        Ticket result = service.createTicket("event123", TicketType.VIP, 200.0, 50);

        assertEquals(ticket, result);
        verify(factory).createTicket("event123", TicketType.VIP, 200.0, 50);
        verify(repository).save(ticket);
    }

    @Test
    public void testDeleteTicketExecutesDeleteCommand() {
        service.deleteTicket("ticket123");
        verify(repository).deleteById("ticket123");
    }

    @Test
    public void testViewTicketReturnsTicket() {
        Ticket ticket = new Ticket();
        when(repository.findById("ticket123")).thenReturn(Optional.of(ticket));

        Ticket result = service.viewTicket("ticket123");

        assertNotNull(result);
        assertEquals(ticket, result);
        verify(repository).findById("ticket123");
    }

    @Test
    public void testViewTicketsByEventReturnsListOfTickets() {
        List<Ticket> tickets = List.of(new Ticket(), new Ticket());
        when(repository.findByEventId("event123")).thenReturn(tickets);

        List<Ticket> result = service.viewTicketsByEvent("event123");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByEventId("event123");
    }

    @Test
    public void testUpdateTicketExecutesCommand() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("price", 250.0);
        updates.put("quota", 100);

        Ticket existingTicket = new Ticket();
        existingTicket.setId("ticket123");
        existingTicket.setPrice(200.0);
        existingTicket.setQuota(50);

        when(repository.findById("ticket123")).thenReturn(Optional.of(existingTicket));

        service.updateTicket("ticket123", updates);

        verify(repository).save(any(Ticket.class));  // ← FIXED
    }
}
