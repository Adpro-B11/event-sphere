package id.ac.ui.cs.advprog.eventsphere.ticket.repository;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class TicketRepository {
    private Map<String, Ticket> tickets = new HashMap<>();

    public void save(Ticket ticket) {
        tickets.put(ticket.getId(), ticket);
    }

    public Ticket findById(String id) {
        Ticket ticket = tickets.get(id);
        if (ticket == null) {
            return null;
        }
        return ticket;
    }

    public List<Ticket> findByEventId(String eventId) {
        List<Ticket> eventTickets = new ArrayList<>();
        for (Ticket ticket : tickets.values()) {
            if (ticket.getEventId().equals(eventId) && ticket.isActive()) {
                eventTickets.add(ticket);
            }
        }
        return eventTickets;
    }

    public void update(Ticket ticket) {
        if (!tickets.containsKey(ticket.getId())) {
            return;
        }
        tickets.put(ticket.getId(), ticket);
    }

    public void delete(String id) {
        Ticket ticket = findById(id);
        // Soft delete - just mark as inactive
        ticket.setActive(false);
        tickets.put(id, ticket);
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(tickets.values());
    }
}