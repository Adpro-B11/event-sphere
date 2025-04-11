package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TicketServiceImplTest {

    private TicketRepository repository;
    private TicketFactory factory;
    private TicketServiceImpl service;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        repository = mock(TicketRepository.class);
        factory = mock(TicketFactory.class);
        service = new TicketServiceImpl(repository, factory);
        mockUser = new User();
    }

    @Test
    public void testCreateTicketExecutesCommandAndReturnsTicket() {
        Ticket ticket = new Ticket();
        when(factory.createTicket("event123", TicketType.VIP, 200.0, 50)).thenReturn(ticket);

        Ticket result = service.createTicket(mockUser, "event123", TicketType.VIP, 200.0, 50);

        assertEquals(ticket, result);
        verify(factory).createTicket("event123", TicketType.VIP, 200.0, 50);
        verify(repository).save(ticket);
    }

    @Test
    public void testDeleteTicketExecutesDeleteCommand() {
        service.deleteTicket(mockUser, "ticket123");

        verify(repository).delete("ticket123");
    }
}
