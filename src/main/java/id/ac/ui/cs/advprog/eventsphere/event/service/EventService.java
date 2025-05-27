package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    void updateStatus(String eventId, String status);
    Event findById(String eventId);
    List<Event> findAllByOrganizer(String organizer);
    void updateEventInfo(String eventId, Event updatedEvent);
    void deleteEvent(String eventId);
    List<Event> findAllEvents();
    boolean isEventFinished(String eventId);
    @Async
    CompletableFuture<Event> createEventAsync(Event event);

    @Async
    CompletableFuture<Event> updateStatusAsync(String eventId, String status);

    @Async
    CompletableFuture<Void> deleteEventAsync(String eventId);
}