package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.command.*;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketFactory factory;

    public TicketServiceImpl(TicketRepository repository, TicketFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public Ticket createTicket(User user, String eventId, TicketType type, double price, int quota) {
        Ticket ticket = factory.createTicket(eventId, type, price, quota);
        TicketCommand command = new CreateTicketCommand(repository, ticket);
        command.execute();
        return ticket;
    }

    @Override
    public void deleteTicket(User user, String ticketId) {
        TicketCommand command = new DeleteTicketCommand(repository, ticketId);
        command.execute();
    }
}
