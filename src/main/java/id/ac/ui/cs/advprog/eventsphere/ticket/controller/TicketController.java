package id.ac.ui.cs.advprog.eventsphere.ticket.controller;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.CreateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.TicketResponse;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.UpdateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketResponse> responses = tickets.stream()
                .map(TicketResponse::fromTicket)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable String ticketId) {
        Ticket ticket = ticketService.viewTicket(ticketId);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TicketResponse.fromTicket(ticket));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByEventId(@PathVariable String eventId) {
        List<Ticket> tickets = ticketService.viewTicketsByEvent(eventId);
        List<TicketResponse> responses = tickets.stream()
                .map(TicketResponse::fromTicket)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(User currentUser,
            @Valid @RequestBody CreateTicketRequest request) {

        Ticket createdTicket = ticketService.createTicket(
                currentUser,
                request.getEventId(),
                request.getType(),
                request.getPrice(),
                request.getQuota()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TicketResponse.fromTicket(createdTicket));
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Void> updateTicket(User currentUser,
            @PathVariable String ticketId,
            @Valid @RequestBody UpdateTicketRequest request) {

        Map<String, Object> updates = new HashMap<>();

        if (request.getType() != null) {
            updates.put("type", request.getType());
        }

        if (request.getPrice() != null) {
            updates.put("price", request.getPrice());
        }

        if (request.getQuota() != null) {
            updates.put("quota", request.getQuota());
        }

        ticketService.updateTicket(currentUser, ticketId, updates);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(User currentUser,
            @PathVariable String ticketId) {

        ticketService.deleteTicket(currentUser, ticketId);
        return ResponseEntity.noContent().build();
    }
}