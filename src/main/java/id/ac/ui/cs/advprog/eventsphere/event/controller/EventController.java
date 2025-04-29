package id.ac.ui.cs.advprog.eventsphere.event.controller;

import id.ac.ui.cs.advprog.eventsphere.event.command.CreateEventCommand;
import id.ac.ui.cs.advprog.eventsphere.event.command.DeleteEventCommand;
import id.ac.ui.cs.advprog.eventsphere.event.command.UpdateStatusCommand;
import id.ac.ui.cs.advprog.eventsphere.event.command.UpdateEventInfoCommand;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Void> createEvent(@RequestBody Event event) {
        new CreateEventCommand(eventService, event).execute();
        return ResponseEntity
                .created(URI.create("/events/" + event.getId()))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        try {
            Event e = eventService.findById(id);
            return ResponseEntity.ok(e);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable String id,
            @RequestBody StatusDto dto
    ) {
        try {
            new UpdateStatusCommand(eventService, id, dto.status).execute();
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        try {
            new DeleteEventCommand(eventService, id).execute();
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEventInfo(
            @PathVariable String id,
            @RequestBody Event updatedEvent
    ) {
        try {
            new UpdateEventInfoCommand(eventService, id, updatedEvent).execute();
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException | IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    public static class StatusDto { public String status; }
}
