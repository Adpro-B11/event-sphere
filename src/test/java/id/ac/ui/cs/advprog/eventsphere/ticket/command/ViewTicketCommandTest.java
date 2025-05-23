package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewTicketCommandTest {

    @Mock
    private TicketRepository repository;

    private String ticketId;
    private Ticket mockTicket;
    private ViewTicketCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ticketId = "ticket-123";
        mockTicket = new Ticket();
        mockTicket.setId(ticketId);
        mockTicket.setType(TicketType.VIP);

        when(repository.findById(ticketId)).thenReturn(Optional.of(mockTicket));

        command = new ViewTicketCommand(repository, ticketId);
    }

    @Test
    void testExecute() {
        command.execute();

        verify(repository, times(1)).findById(ticketId);
        assertEquals(mockTicket, command.getResult());
    }
}
