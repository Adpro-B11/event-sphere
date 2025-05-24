package id.ac.ui.cs.advprog.eventsphere.ticket.controller;

import id.ac.ui.cs.advprog.eventsphere.ticket.dto.CreateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.TicketResponse;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.UpdateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public CompletableFuture<ResponseEntity<List<TicketResponse>>> getAllTickets() {
        return CompletableFuture.supplyAsync(() -> {
            List<Ticket> tickets = ticketService.getAllTickets();
            List<TicketResponse> responses = tickets.stream()
                    .map(TicketResponse::fromTicket)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        });
    }

    @GetMapping("/{ticketId}")
    public CompletableFuture<ResponseEntity<TicketResponse>> getTicketById(@PathVariable String ticketId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Ticket ticket = ticketService.viewTicket(ticketId);
                if (ticket == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(TicketResponse.fromTicket(ticket));
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @GetMapping("/event/{eventId}")
    public CompletableFuture<ResponseEntity<List<TicketResponse>>> getTicketsByEventId(@PathVariable String eventId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Ticket> tickets = ticketService.viewTicketsByEvent(eventId);
            List<TicketResponse> responses = tickets.stream()
                    .map(TicketResponse::fromTicket)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responses);
        });
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public CompletableFuture<ResponseEntity<TicketResponse>> createTicket(@Valid @RequestBody CreateTicketRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Ticket createdTicket = ticketService.createTicket(
                    request.getEventId(),
                    request.getType(),
                    request.getPrice(),
                    request.getQuota()
            );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(TicketResponse.fromTicket(createdTicket));
        });
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @PutMapping("/{ticketId}")
    public CompletableFuture<ResponseEntity<Void>> updateTicket(@PathVariable String ticketId,
                                                                @Valid @RequestBody UpdateTicketRequest request) {
        return CompletableFuture.supplyAsync(() -> {
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
            try {
                ticketService.updateTicket(ticketId, updates);
                return ResponseEntity.noContent().build();
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            } catch (SecurityException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        });
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public CompletableFuture<ResponseEntity<Void>> deleteTicket(@PathVariable String ticketId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ticketService.deleteTicket(ticketId);
                return ResponseEntity.noContent().build();
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            } catch (SecurityException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        });
    }
}