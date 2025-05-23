package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;

import java.util.List;

public interface EventService {
    void createEvent(Event event);
    void updateStatus(String eventId, String status);
    Event findById(String eventId);
    List<Event> findAllByOrganizer(String organizer);

    // NEW
    void updateEventInfo(String eventId, Event updatedEvent);
    void deleteEvent(String eventId);
    List<Event> findAllEvents();

    @Async
    default CompletableFuture<Void> updateStatusAsync(String eventId, String status) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Async
    default CompletableFuture<Void> deleteEventAsync(String eventId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Async
    default CompletableFuture<Void> createEventAsync(Event event) {
        throw new UnsupportedOperationException("Not implemented yet");
    }




}