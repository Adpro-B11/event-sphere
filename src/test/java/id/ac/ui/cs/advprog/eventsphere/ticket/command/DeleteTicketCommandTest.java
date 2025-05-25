package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DeleteTicketCommandTest {

    private TicketRepository repository;
    private DeleteTicketCommand command;
    private final String ticketId = "ticket123";

    @BeforeEach
    void setUp() {
        repository = mock(TicketRepository.class);
        command = new DeleteTicketCommand(repository, ticketId);
    }

    @Test
    void testExecuteDeletesTicketFromRepository() {
        command.execute();

        verify(repository, times(1)).deleteById(ticketId);
    }
}
