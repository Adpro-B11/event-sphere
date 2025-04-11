package id.ac.ui.cs.advprog.eventsphere.event.repository;

import id.ac.ui.cs.advprog.eventsphere.event.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private List<Event> events = new ArrayList<>();

    public void save(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId().equals(event.getId())) {
                events.set(i, event);
                return;
            }
        }
        events.add(event);
    }

    public Event findById(String id) {
        for (Event event : events) {
            if (event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }

    public List<Event> findAllByOrganizer(String organizer) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getOrganizer() != null && event.getOrganizer().equals(organizer)) {
                result.add(event);
            }
        }
        return result;
    }
}
