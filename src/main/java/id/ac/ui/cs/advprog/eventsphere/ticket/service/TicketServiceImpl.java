package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.command.*;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.List;
import java.util.Map;

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

    public Ticket viewTicket(String ticketId) {
        ViewTicketCommand command = new ViewTicketCommand(repository, ticketId);
        command.execute();
        return command.getResult();
    }

    public List<Ticket> viewTicketsByEvent(String eventId) {
        ViewEventTicketsCommand command = new ViewEventTicketsCommand(repository, eventId);
        command.execute();
        return command.getResult();
    }

    public void updateTicket(User user, String ticketId, Map<String, Object> updates) {
        // blm implement user role
//        if (user.getRole() != Role.ORGANIZER) {
//            throw new UnauthorizedException("Only organizers can update tickets");
//        }

        TicketCommand command = new UpdateTicketCommand(repository, ticketId, updates);
        command.execute();
    }

    @Override
    public void deleteTicket(User user, String ticketId) {
        TicketCommand command = new DeleteTicketCommand(repository, ticketId);
        command.execute();
    }
}
