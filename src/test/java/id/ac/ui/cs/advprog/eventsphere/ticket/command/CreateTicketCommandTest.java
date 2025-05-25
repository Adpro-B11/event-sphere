package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class CreateTicketCommandTest {

    private TicketRepository repository;
    private Ticket ticket;
    private CreateTicketCommand command;

    @BeforeEach
    void setUp() {
        repository = mock(TicketRepository.class);
        ticket = new Ticket();
        command = new CreateTicketCommand(repository, ticket);
    }

    @Test
    void testExecuteSavesTicketToRepository() {
        command.execute();

        verify(repository, times(1)).save(ticket);
    }
}
