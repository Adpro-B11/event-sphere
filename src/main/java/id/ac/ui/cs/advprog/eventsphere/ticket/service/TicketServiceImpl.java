package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.auth.model.User;
import id.ac.ui.cs.advprog.eventsphere.ticket.command.*;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketCommandFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketFactory ticketFactory;
    private final TicketCommandFactory commandFactory;

    public TicketServiceImpl(TicketRepository repository, TicketFactory ticketFactory) {
        this.repository = repository;
        this.ticketFactory = ticketFactory;
        this.commandFactory = new TicketCommandFactory(repository);
    }

    @Override
    public Ticket createTicket(User user, String eventId, TicketType type, double price, int quota) {
        Ticket ticket = ticketFactory.createTicket(eventId, type, price, quota);
        TicketCommand command = commandFactory.createTicketCommand(ticket);
        command.execute();
        return ticket;
    }

    @Override
    public Ticket viewTicket(String ticketId) {
        ViewTicketCommand command = commandFactory.viewTicketCommand(ticketId);
        command.execute();
        return command.getResult();
    }

    @Override
    public List<Ticket> viewTicketsByEvent(String eventId) {
        ViewEventTicketsCommand command = commandFactory.viewEventTicketsCommand(eventId);
        command.execute();
        return command.getResult();
    }

    @Override
    public void updateTicket(User user, String ticketId, Map<String, Object> updates) {
        TicketCommand command = commandFactory.updateTicketCommand(ticketId, updates);
        command.execute();
    }

    @Override
    public void deleteTicket(User user, String ticketId) {
        // blm implement user role
        // if (user.getRole() != Role.ORGANIZER) {
        //     throw new UnauthorizedException("Only organizers can delete tickets");
        // }

        TicketCommand command = commandFactory.deleteTicketCommand(ticketId);
        command.execute();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return repository.findAll();
    }
}