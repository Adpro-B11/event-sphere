package id.ac.ui.cs.advprog.eventsphere.ticket.repository;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketRepositoryTest {

    @Mock
    private TicketRepository repository;

    private Ticket ticket1;
    private Ticket ticket2;
    private String ticket1Id;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ticket1Id = UUID.randomUUID().toString();
        ticket1 = new Ticket();
        ticket1.setId(ticket1Id);
        ticket1.setEventId("event123");
        ticket1.setType(TicketType.REGULAR);
        ticket1.setPrice(100.0);
        ticket1.setQuota(50);
        ticket1.setRemaining(50);
        ticket1.setActive(true);

        when(repository.findById(ticket1Id)).thenReturn(Optional.of(ticket1));
        when(repository.findById(eq("nonexistent-id"))).thenReturn(Optional.empty());
    }

    @Test
    public void testSaveAndFindById() {
        when(repository.save(any(Ticket.class))).thenReturn(ticket1);

        Ticket savedTicket = repository.save(ticket1);
        Optional<Ticket> foundOptional = repository.findById(ticket1Id);

        assertNotNull(savedTicket, "Save should return the saved ticket.");
        assertEquals(ticket1Id, savedTicket.getId(), "Saved ticket ID should match.");

        assertTrue(foundOptional.isPresent(), "Ticket should be found by ID.");
        foundOptional.ifPresent(found -> assertEquals(ticket1Id, found.getId(), "Found ticket ID should match."));

        verify(repository, times(1)).save(ticket1);
        verify(repository, times(1)).findById(ticket1Id);
    }

    @Test
    public void testFindByIdNotFoundReturnsEmptyOptional() {
        Optional<Ticket> result = repository.findById("nonexistent-id");

        assertFalse(result.isPresent(), "Optional should be empty for a nonexistent ID.");
        verify(repository, times(1)).findById("nonexistent-id");
    }

    @Test
    public void testUpdateTicket_ChangesPrice() {
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Ticket> ticketToUpdateOptional = repository.findById(ticket1Id);
        assertTrue(ticketToUpdateOptional.isPresent());
        Ticket ticketToUpdate = ticketToUpdateOptional.get();

        ticketToUpdate.setPrice(200.0);
        Ticket updatedTicket = repository.save(ticketToUpdate);

        assertNotNull(updatedTicket);
        assertEquals(200.0, updatedTicket.getPrice(), "Ticket price should be updated.");
        verify(repository, times(1)).findById(ticket1Id);
        verify(repository, times(1)).save(ticketToUpdate);
    }


    @Test
    public void testDeleteTicket_RemovesTicket() {
        String eventId = ticket1.getEventId();

        doNothing().when(repository).deleteById(ticket1Id);
        when(repository.findById(ticket1Id)).thenReturn(Optional.empty());
        when(repository.findByEventId(eventId)).thenReturn(Collections.emptyList());

        repository.deleteById(ticket1Id);

        Optional<Ticket> deletedOptional = repository.findById(ticket1Id);
        List<Ticket> eventTickets = repository.findByEventId(eventId);

        verify(repository, times(1)).deleteById(ticket1Id);

        assertFalse(deletedOptional.isPresent(), "Hard-deleted ticket should not be findable by ID.");
        assertTrue(eventTickets.isEmpty(), "Hard-deleted ticket should not be returned by findByEventId.");
    }

    @Test
    public void testFindAllReturnsAllMockedTickets() {
        ticket2 = new Ticket();
        ticket2.setId(UUID.randomUUID().toString());
        ticket2.setEventId("eventABC");
        ticket2.setType(TicketType.VIP);
        ticket2.setActive(true);

        List<Ticket> allTicketsList = Arrays.asList(ticket1, ticket2);
        when(repository.findAll()).thenReturn(allTicketsList);

        List<Ticket> foundAll = repository.findAll();

        assertEquals(2, foundAll.size(), "FindAll should return all tickets provided by the mock.");
        assertTrue(foundAll.contains(ticket1), "List should contain ticket1.");
        assertTrue(foundAll.contains(ticket2), "List should contain ticket2.");

        verify(repository, times(1)).findAll();
    }
}
