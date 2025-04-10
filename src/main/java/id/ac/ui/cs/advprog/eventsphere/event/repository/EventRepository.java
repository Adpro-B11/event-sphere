package id.ac.ui.cs.advprog.eventsphere.event.repository;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private List<Event> events = new ArrayList<>();

    public void save(Event event) {
        // Placeholder implementation
    }

    public Event findById(String id) {
        return null; // Placeholder implementation
    }

    public List<Event> findAllByOrganizer(String organizer) {
        return new ArrayList<>(); // Placeholder implementation
    }
}
