package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import java.util.concurrent.CompletableFuture;


@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void createEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        eventRepository.save(event);
    }

    @Override
    public void updateStatus(String eventId, String status) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            throw new NoSuchElementException("Event with ID " + eventId + " not found");
        }
        if (!EventStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        event.setStatus(status);
        eventRepository.save(event);
    }

    @Override
    public Event findById(String eventId) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            throw new NoSuchElementException("Event with ID " + eventId + " not found");
        }
        return event;
    }

    @Override
    public List<Event> findAllByOrganizer(String organizer) {
        return eventRepository.findAllByOrganizer(organizer);
    }

    @Override
    public void updateEventInfo(String eventId, Event updatedEvent) {
        Event event = eventRepository.findById(eventId);
        if (event == null) {
            throw new NoSuchElementException("Event with ID " + eventId + " not found");
        }
        LocalDate eventDate = LocalDate.parse(event.getDate());
        if (LocalDate.now().isAfter(eventDate)) {
            throw new IllegalStateException("Cannot update event after its date");
        }
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setDate(updatedEvent.getDate());
        event.setLocation(updatedEvent.getLocation());
        event.setPrice(updatedEvent.getPrice());
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(String eventId) {
        boolean removed = eventRepository.deleteById(eventId);
        if (!removed) {
            throw new NoSuchElementException("Event with ID " + eventId + " not found");
        }
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Async
    public CompletableFuture<Void> updateStatusAsync(String eventId, String status) {
        try {
            updateStatus(eventId, status);
            return CompletableFuture.completedFuture(null);
        } catch (Exception ex) {
            CompletableFuture<Void> cf = new CompletableFuture<>();
            cf.completeExceptionally(ex);
            return cf;
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> deleteEventAsync(String eventId) {
        try {
            deleteEvent(eventId);
            return CompletableFuture.completedFuture(null);
        } catch (Exception ex) {
            CompletableFuture<Void> cf = new CompletableFuture<>();
            cf.completeExceptionally(ex);
            return cf;
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> createEventAsync(Event event) {
        try {
            createEvent(event);
            return CompletableFuture.completedFuture(null);
        } catch (Exception ex) {
            CompletableFuture<Void> cf = new CompletableFuture<>();
            cf.completeExceptionally(ex);
            return cf;
        }
    }
}