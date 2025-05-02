package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateTicketCommandTest {

    @Mock
    private TicketRepository repository;

    private String ticketId;
    private Ticket mockTicket;
    private Map<String, Object> updates;
    private UpdateTicketCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ticketId = "ticket-123";
        mockTicket = new Ticket();
        mockTicket.setId(ticketId);
        mockTicket.setType(TicketType.REGULAR);
        mockTicket.setPrice(100.0);
        mockTicket.setQuota(200);
        mockTicket.setRemaining(200);

        when(repository.findById(ticketId)).thenReturn(mockTicket);

        updates = new HashMap<>();
        updates.put("price", 120.0);

        command = new UpdateTicketCommand(repository, ticketId, updates);
    }

    @Test
    void testExecute_UpdatePrice() {
        command.execute();

        verify(repository, times(1)).findById(ticketId);
        verify(repository, times(1)).update(mockTicket);
        assertEquals(120.0, mockTicket.getPrice());
    }

    @Test
    void testExecute_UpdateQuota() {
        updates.clear();
        updates.put("quota", 300);
        command = new UpdateTicketCommand(repository, ticketId, updates);

        command.execute();

        verify(repository, times(1)).update(mockTicket);
        assertEquals(300, mockTicket.getQuota());
        assertEquals(300, mockTicket.getRemaining()); // Remaining should increase by 100
    }

    @Test
    void testExecute_UpdateType() {
        updates.clear();
        updates.put("type", TicketType.VIP);
        command = new UpdateTicketCommand(repository, ticketId, updates);

        command.execute();

        verify(repository, times(1)).update(mockTicket);
        assertEquals(TicketType.VIP, mockTicket.getType());
    }

    @Test
    void testExecute_MultipleUpdates() {
        updates.put("quota", 250);
        updates.put("type", TicketType.STUDENT);
        command = new UpdateTicketCommand(repository, ticketId, updates);

        command.execute();

        verify(repository, times(1)).update(mockTicket);
        assertEquals(120.0, mockTicket.getPrice());
        assertEquals(250, mockTicket.getQuota());
        assertEquals(250, mockTicket.getRemaining());
        assertEquals(TicketType.STUDENT, mockTicket.getType());
    }
}
