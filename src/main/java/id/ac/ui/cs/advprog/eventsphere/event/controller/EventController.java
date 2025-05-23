package id.ac.ui.cs.advprog.eventsphere.event.controller;

import id.ac.ui.cs.advprog.eventsphere.event.command.CreateEventCommand;
import id.ac.ui.cs.advprog.eventsphere.event.command.DeleteEventCommand;
import id.ac.ui.cs.advprog.eventsphere.event.command.UpdateEventInfoCommand;
import id.ac.ui.cs.advprog.eventsphere.event.command.UpdateStatusCommand;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Void>> createEvent(@RequestBody Event event) {
        return CompletableFuture.supplyAsync(() -> {
            new CreateEventCommand(eventService, event).execute();
            // note: Location header stays "/events/{id}"
            URI location = URI.create("/events/" + event.getId());
            return ResponseEntity.created(location).build();
        });
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Event>> getEventById(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Event e = eventService.findById(id);
                return ResponseEntity.ok(e);
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<Event>>> getAllEvents() {
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.ok(eventService.findAllEvents())
        );
    }

    @PutMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> updateEventInfo(
            @PathVariable String id,
            @RequestBody Event updatedEvent
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                new UpdateEventInfoCommand(eventService, id, updatedEvent).execute();
                return ResponseEntity.ok().build();
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            } catch (IllegalStateException | IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }
        });
    }

    @PatchMapping("/{id}/status")
    public CompletableFuture<ResponseEntity<Void>> updateStatus(
            @PathVariable String id,
            @RequestBody StatusDto dto
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                new UpdateStatusCommand(eventService, id, dto.status).execute();
                return ResponseEntity.ok().build();
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }
        });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Void>> deleteEvent(@PathVariable String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                new DeleteEventCommand(eventService, id).execute();
                return ResponseEntity.noContent().build();
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            }
        });
    }

    public static class StatusDto {
        public String status;
    }
}
