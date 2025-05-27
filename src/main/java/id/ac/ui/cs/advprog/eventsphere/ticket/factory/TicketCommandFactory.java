package id.ac.ui.cs.advprog.eventsphere.ticket.factory;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import id.ac.ui.cs.advprog.eventsphere.ticket.command.*;

import java.util.Map;

public class TicketCommandFactory {
    private final TicketRepository repository;

    public TicketCommandFactory(TicketRepository repository) {
        this.repository = repository;
    }

    public TicketCommand createTicketCommand(Ticket ticket) {
        return new CreateTicketCommand(repository, ticket);
    }

    public TicketCommand deleteTicketCommand(String ticketId) {
        return new DeleteTicketCommand(repository, ticketId);
    }

    public ViewTicketCommand viewTicketCommand(String ticketId) {
        return new ViewTicketCommand(repository, ticketId);
    }

    public ViewEventTicketsCommand viewEventTicketsCommand(String eventId) {
        return new ViewEventTicketsCommand(repository, eventId);
    }

    public TicketCommand updateTicketCommand(String ticketId, Map<String, Object> updates) {
        return new UpdateTicketCommand(repository, ticketId, updates);
    }

    public DecreaseQuotaCommand decreaseQuotaCommand(String ticketId, int quantity) {
        return new DecreaseQuotaCommand(repository,ticketId, quantity);
    }
    public TicketCommand decreaseQuotaBatchCommand(Map<String, String> ticketQuantities) {
        return new DecreaseTicketQuotaBatchCommand(repository, ticketQuantities);
    }
}