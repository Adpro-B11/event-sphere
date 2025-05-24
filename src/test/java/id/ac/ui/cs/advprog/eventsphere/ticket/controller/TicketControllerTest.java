package id.ac.ui.cs.advprog.eventsphere.ticket.controller;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.CreateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.TicketResponse;
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
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        testUser = new User();
        testUser.setId(UUID.fromString("a1b2c3d4-e5f6-7788-9900-aabbccddeeff"));

        testTicket = new Ticket();
        testTicket.setId("ticket123");
        testTicket.setEventId("event123");
        testTicket.setType(TicketType.VIP);
        testTicket.setPrice(100.0);
        testTicket.setQuota(50);
        testTicket.setRemaining(50);
        testTicket.setActive(true);

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
    void getAllTickets_returnsListOfTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(testTickets);

        CompletableFuture<ResponseEntity<List<TicketResponse>>> futureResponse = ticketController.getAllTickets();
        ResponseEntity<List<TicketResponse>> response = futureResponse.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(ticketService).getAllTickets();
    }

    @Test
    void getTicketById_whenExists_returnsTicket() throws Exception {
        when(ticketService.viewTicket("ticket123")).thenReturn(testTicket);

        CompletableFuture<ResponseEntity<TicketResponse>> futureResponse = ticketController.getTicketById("ticket123");
        ResponseEntity<TicketResponse> response = futureResponse.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ticket123", response.getBody().getId());
        assertEquals(TicketType.VIP, response.getBody().getType());
        assertEquals(100.0, response.getBody().getPrice());
        assertEquals(50, response.getBody().getQuota());
        assertEquals(50, response.getBody().getRemaining());
        assertEquals(true, response.getBody().isActive());
    }

    @Test
    void getTicketById_whenNotExists_returnsNotFound() throws Exception {
        when(ticketService.viewTicket("nonexistent")).thenReturn(null);

        CompletableFuture<ResponseEntity<TicketResponse>> futureResponse = ticketController.getTicketById("nonexistent");
        ResponseEntity<TicketResponse> response = futureResponse.get();


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getTicketsByEventId_returnsTicketsForEvent() throws Exception {
        when(ticketService.viewTicketsByEvent("event123")).thenReturn(testTickets);

        CompletableFuture<ResponseEntity<List<TicketResponse>>> futureResponse = ticketController.getTicketsByEventId("event123");
        ResponseEntity<List<TicketResponse>> response = futureResponse.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createTicket_returnsCreatedTicket() throws Exception {
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

        CompletableFuture<ResponseEntity<TicketResponse>> futureResponse = ticketController.createTicket(testUser, request);
        ResponseEntity<TicketResponse> response = futureResponse.get();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("newTicket", response.getBody().getId());
        assertEquals(TicketType.VIP, response.getBody().getType());
        assertEquals(150.0, response.getBody().getPrice());
        assertEquals(25, response.getBody().getQuota());
    }

    @Test
    void updateTicket_returnsNoContent() throws Exception {
        UpdateTicketRequest request = new UpdateTicketRequest();
        request.setType(TicketType.EARLY_BIRD);
        request.setPrice(200.0);

        doNothing().when(ticketService).updateTicket(eq(testUser), eq("ticket123"), anyMap());


        CompletableFuture<ResponseEntity<Void>> futureResponse = ticketController.updateTicket(testUser, "ticket123", request);
        ResponseEntity<Void> response = futureResponse.get();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService).updateTicket(eq(testUser), eq("ticket123"), anyMap());
    }

    @Test
    void deleteTicket_returnsNoContent() throws Exception {
        doNothing().when(ticketService).deleteTicket(testUser, "ticket123");

        CompletableFuture<ResponseEntity<Void>> futureResponse = ticketController.deleteTicket(testUser, "ticket123");
        ResponseEntity<Void> response = futureResponse.get();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(ticketService).deleteTicket(testUser, "ticket123");
    }
}
