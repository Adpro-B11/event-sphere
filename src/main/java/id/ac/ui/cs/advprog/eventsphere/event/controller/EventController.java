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
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Event>> createEvent(@RequestBody Event event) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                new CreateEventCommand(eventService, event).execute();
                // Return the saved event with its ID
                Event savedEvent = eventService.findById(event.getId());
                return ResponseEntity.ok(savedEvent);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }
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
    public CompletableFuture<ResponseEntity<Event>> updateEventInfo(
            @PathVariable String id,
            @RequestBody Event updatedEvent
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                new UpdateEventInfoCommand(eventService, id, updatedEvent).execute();
                Event updated = eventService.findById(id);
                return ResponseEntity.ok(updated);
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            } catch (IllegalStateException | IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }
        });
    }
    @PatchMapping("/{id}/status")
    public CompletableFuture<ResponseEntity<Event>> updateStatus(@PathVariable String id, @RequestBody StatusDto dto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                new UpdateStatusCommand(eventService, id, dto.status).execute();
                // Return the updated event
                Event updatedEvent = eventService.findById(id);
                return ResponseEntity.ok(updatedEvent);
            } catch (NoSuchElementException ex) {
                return ResponseEntity.notFound().build();
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().build();
            }
        });
    }

    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<Event>> deleteEvent(@PathVariable String id) {
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
