package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketService {
    Ticket createTicket(User user, String eventId, TicketType type, double price, int quota);
    void deleteTicket(User user, String ticketId);
    Ticket viewTicket(String ticketId);
    List<Ticket> viewTicketsByEvent(String eventId);
}
