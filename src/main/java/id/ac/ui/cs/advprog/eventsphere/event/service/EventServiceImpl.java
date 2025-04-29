package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;
import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;

import java.util.List;
import java.util.NoSuchElementException;

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
    }

    @Override
    public void deleteEvent(String eventId) {
    }

    @Override
    public List<Event> findAllEvents() {
        return null;
    }

}