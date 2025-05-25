package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DecreaseQuotaCommandTest {

    private TicketRepository repository;
    private final String eventId = "event123";
    private final TicketType ticketType = TicketType.REGULAR;
    private final int quantity = 2;

    @BeforeEach
    void setUp() {
        repository = mock(TicketRepository.class);
    }

    @Test
    void testExecuteShouldDecreaseQuotaAndRemaining() {
        Ticket ticket = new Ticket();
        ticket.setType(ticketType);
        ticket.setQuota(10);
        ticket.setRemaining(8);

        when(repository.findByEventId(eventId)).thenReturn(List.of(ticket));

        DecreaseQuotaCommand command = new DecreaseQuotaCommand(repository, eventId, ticketType, quantity);
        command.execute();

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(repository).save(ticketCaptor.capture());

        Ticket savedTicket = ticketCaptor.getValue();
        assertEquals(8, savedTicket.getQuota());
        assertEquals(6, savedTicket.getRemaining());
    }

    @Test
    void testExecuteShouldThrowExceptionWhenTicketNotFound() {
        when(repository.findByEventId(eventId)).thenReturn(Collections.emptyList());

        DecreaseQuotaCommand command = new DecreaseQuotaCommand(repository, eventId, ticketType, quantity);

        assertThrows(NoSuchElementException.class, command::execute);
    }

    @Test
    void testExecuteShouldThrowExceptionWhenQuotaInsufficient() {
        Ticket ticket = new Ticket();
        ticket.setType(ticketType);
        ticket.setQuota(1);
        ticket.setRemaining(1);

        when(repository.findByEventId(eventId)).thenReturn(List.of(ticket));

        DecreaseQuotaCommand command = new DecreaseQuotaCommand(repository, eventId, ticketType, quantity);

        RuntimeException ex = assertThrows(RuntimeException.class, command::execute);
        assertTrue(ex.getMessage().contains("Insufficient quota"));
    }

    @Test
    void testRemainingShouldNotGoBelowZero() {
        Ticket ticket = new Ticket();
        ticket.setType(ticketType);
        ticket.setQuota(5);
        ticket.setRemaining(1);

        when(repository.findByEventId(eventId)).thenReturn(List.of(ticket));

        DecreaseQuotaCommand command = new DecreaseQuotaCommand(repository, eventId, ticketType, quantity);
        command.execute();

        ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
        verify(repository).save(ticketCaptor.capture());

        Ticket savedTicket = ticketCaptor.getValue();
        assertEquals(3, savedTicket.getQuota());
        assertEquals(0, savedTicket.getRemaining());
    }
}
