package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventService {
    // sync
    Event createEvent(Event event);
    void updateStatus(String eventId, String status);
    Event findById(String eventId);
    List<Event> findAllByOrganizer(String organizer);
    void updateEventInfo(String eventId, Event updatedEvent);
    void deleteEvent(String eventId);
    List<Event> findAllEvents();

    // async – return Event agar tidak null
    @Async
    CompletableFuture<Event> createEventAsync(Event event);

    @Async
    CompletableFuture<Event> updateStatusAsync(String eventId, String status);

    @Async
    CompletableFuture<Void> deleteEventAsync(String eventId);
}