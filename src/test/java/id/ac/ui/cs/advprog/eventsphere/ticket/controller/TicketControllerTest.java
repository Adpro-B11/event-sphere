package id.ac.ui.cs.advprog.eventsphere.ticket.controller;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.CreateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.UpdateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private User testUser;
    private Ticket testTicket;
    private List<Ticket> testTickets;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(UUID.fromString("user123"));

        // Create test ticket
        testTicket = new Ticket();
        testTicket.setId("ticket123");
        testTicket.setEventId("event123");
        testTicket.setType(TicketType.VIP);
        testTicket.setPrice(100.0);
        testTicket.setQuota(50);
        testTicket.setRemaining(50);
        testTicket.setActive(true);

        // Create list of test tickets
        Ticket testTicket2 = new Ticket();
        testTicket2.setId("ticket456");
        testTicket2.setEventId("event123");
        testTicket2.setType(TicketType.REGULAR);
        testTicket2.setPrice(50.0);
        testTicket2.setQuota(100);
        testTicket2.setRemaining(75);
        testTicket2.setActive(true);

        testTickets = List.of(testTicket, testTicket2);
    }

    @Test
    void getAllTickets_returnsListOfTickets() {
        when(ticketService.getAllTickets()).thenReturn(testTickets);

        var response = ticketController.getAllTickets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(ticketService).getAllTickets();
    }

    @Test
    void getTicketById_whenExists_returnsTicket() {
        when(ticketService.viewTicket("ticket123")).thenReturn(testTicket);

        var response = ticketController.getTicketById("ticket123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ticket123", response.getBody().getId());
        assertEquals(TicketType.VIP, response.getBody().getType());
        assertEquals(100.0, response.getBody().getPrice());
        assertEquals(50, response.getBody().getQuota());
        assertEquals(50, response.getBody().getRemaining());
        assertEquals(true, response.getBody().isActive());
    }

    @Test
    void getTicketById_whenNotExists_returnsNotFound() {
        when(ticketService.viewTicket("nonexistent")).thenReturn(null);

        var response = ticketController.getTicketById("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getTicketsByEventId_returnsTicketsForEvent() {
        when(ticketService.viewTicketsByEvent("event123")).thenReturn(testTickets);

        var response = ticketController.getTicketsByEventId("event123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createTicket_returnsCreatedTicket() {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setEventId("event123");
        request.setType(TicketType.VIP);
        request.setPrice(150.0);
        request.setQuota(25);

        Ticket newTicket = new Ticket();
        newTicket.setId("newTicket");
        newTicket.setEventId("event123");
        newTicket.setType(TicketType.VIP);
        newTicket.setPrice(150.0);
        newTicket.setQuota(25);
        newTicket.setRemaining(25);
        newTicket.setActive(true);

        when(ticketService.createTicket(
                testUser,
                request.getEventId(),
                request.getType(),
                request.getPrice(),
                request.getQuota())
        ).thenReturn(newTicket);

        var response = ticketController.createTicket(testUser, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("newTicket", response.getBody().getId());
        assertEquals(TicketType.VIP, response.getBody().getType());
        assertEquals(150.0, response.getBody().getPrice());
        assertEquals(25, response.getBody().getQuota());
    }

    @Test
    void updateTicket_returnsNoContent() {
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setType(TicketType.EARLY_BIRD);
        request.setPrice(200.0);

        var response = ticketController.updateTicket(testUser, "ticket123", request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService).updateTicket(eq(testUser), eq("ticket123"), anyMap());
    }

    @Test
    void deleteTicket_returnsNoContent() {
        var response = ticketController.deleteTicket(testUser, "ticket123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService).deleteTicket(testUser, "ticket123");
    }
}