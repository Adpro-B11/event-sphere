package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;

public interface TicketService {
    Ticket createTicket(User user, String eventId, TicketType type, double price, int quota);
    void deleteTicket(User user, String ticketId);
}
