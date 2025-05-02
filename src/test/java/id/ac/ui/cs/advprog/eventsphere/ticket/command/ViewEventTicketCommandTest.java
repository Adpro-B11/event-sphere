package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewEventTicketsCommandTest {

    @Mock
    private TicketRepository repository;

    private String eventId;
    private List<Ticket> mockTickets;
    private ViewEventTicketsCommand command;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        eventId = "event-123";

        Ticket ticket1 = new Ticket();
        ticket1.setEventId(eventId);
        ticket1.setType(TicketType.VIP);

        Ticket ticket2 = new Ticket();
        ticket2.setEventId(eventId);
        ticket2.setType(TicketType.REGULAR);

        mockTickets = Arrays.asList(ticket1, ticket2);

        when(repository.findByEventId(eventId)).thenReturn(mockTickets);

        command = new ViewEventTicketsCommand(repository, eventId);
    }

    @Test
    void testExecute() {
        command.execute();

        verify(repository, times(1)).findByEventId(eventId);
        assertEquals(mockTickets, command.getResult());
        assertEquals(2, command.getResult().size());
    }
}
