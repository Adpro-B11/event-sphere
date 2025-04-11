package id.ac.ui.cs.advprog.eventsphere.event.service;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;
import id.ac.ui.cs.advprog.eventsphere.event.repository.EventRepository;

import java.util.List;

public interface EventService {
    void createEvent(Event event);
    void updateStatus(String eventId, String status);
    Event findById(String eventId);
    List<Event> findAllByOrganizer(String organizer);

    class EventServiceImpl implements EventService {
        private final EventRepository eventRepository;

        public EventServiceImpl(EventRepository eventRepository) {
            this.eventRepository = eventRepository;
        }

        @Override
        public void createEvent(Event event) {
            // Placeholder implementation
        }

        @Override
        public void updateStatus(String eventId, String status) {
            // Placeholder implementation
        }

        @Override
        public Event findById(String eventId) {
            return null; // Placeholder implementation
        }

        @Override
        public List<Event> findAllByOrganizer(String organizer) {
            return null; // Placeholder implementation
        }
    }
}