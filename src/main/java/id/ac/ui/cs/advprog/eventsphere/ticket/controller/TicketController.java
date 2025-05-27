package id.ac.ui.cs.advprog.eventsphere.ticket.controller;

import id.ac.ui.cs.advprog.eventsphere.ticket.dto.CreateTicketRequest;
import id.ac.ui.cs.advprog.eventsphere.ticket.dto.DeductTicketsRequest;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    @PostMapping("/deduct-batch")
    public ResponseEntity<Void> deductBatch(@RequestBody DeductTicketsRequest request) {
        try {
            log.debug("Received deductBatch request: eventId={}, tickets={}", request.getEventId(), request.getTickets());
            Map<String, String> map = request.getTickets();
            String ticketId = request.getEventId();

            // Log map isian
            log.debug("Deducting tickets: ticketId={}, map={}", ticketId, map);

            ticketService.decreaseQuotaBatch(ticketId, map);

            log.info("Batch ticket deduction success for eventId={}, tickets={}", ticketId, map);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException ex) {
            log.warn("Batch ticket deduction failed: Not found. eventId={}, tickets={}", request.getEventId(), request.getTickets());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            log.error("Batch ticket deduction failed: Bad request. eventId={}, tickets={}, error={}", request.getEventId(), request.getTickets(), ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}