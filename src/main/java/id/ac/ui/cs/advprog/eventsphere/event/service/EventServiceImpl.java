package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // synchronous
    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void updateStatus(String eventId, String status) {
        Event evt = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found: " + eventId));
        evt.setStatus(status);
        eventRepository.save(evt);
    }

    @Override
    public Event findById(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found: " + eventId));
    }

    @Override
    public List<Event> findAllByOrganizer(String organizer) {
        return eventRepository.findByOrganizer(organizer);
    }

    @Override
    public void updateEventInfo(String eventId, Event updatedEvent) {
        Event existing = findById(eventId);
        existing.setTitle(updatedEvent.getTitle());
        existing.setDescription(updatedEvent.getDescription());
        existing.setDate(updatedEvent.getDate());
        existing.setLocation(updatedEvent.getLocation());
        existing.setPrice(updatedEvent.getPrice());
        existing.setStatus(updatedEvent.getStatus());
        existing.setOrganizer(updatedEvent.getOrganizer());
        eventRepository.save(existing);
    }

    @Override
    public void deleteEvent(String eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NoSuchElementException("Cannot delete, event not found: " + eventId);
        }
        eventRepository.deleteById(eventId);
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Async
    @Override
    public CompletableFuture<Event> createEventAsync(Event event) {
        Event saved = createEvent(event);
        return CompletableFuture.completedFuture(saved);
    }

    @Async
    @Override
    public CompletableFuture<Event> updateStatusAsync(String eventId, String status) {
        updateStatus(eventId, status);
        Event updated = findById(eventId);
        return CompletableFuture.completedFuture(updated);
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteEventAsync(String eventId) {
        deleteEvent(eventId);
        return CompletableFuture.completedFuture(null);
    }
}
