package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketService {
    Ticket createTicket(String eventId, TicketType type, double price, int quota);
    void deleteTicket(String ticketId);
    Ticket viewTicket(String ticketId);
    List<Ticket> viewTicketsByEvent(String eventId);
    void updateTicket(String ticketId, Map<String, Object> updates);
    List<Ticket> getAllTickets();
    void decreaseQuota(String eventId, String ticketType, int quantity);
    boolean userHasTicket(String userId, String eventId);
    void decreaseQuotaBatch(String eventId, Map<String, String> ticketQuantities);
}