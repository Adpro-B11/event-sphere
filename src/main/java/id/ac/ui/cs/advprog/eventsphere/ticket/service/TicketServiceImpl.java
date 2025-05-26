package id.ac.ui.cs.advprog.eventsphere.ticket.service;

import id.ac.ui.cs.advprog.eventsphere.ticket.command.*;
import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketCommandFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.factory.TicketFactory;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketFactory ticketFactory;
    private final TicketCommandFactory commandFactory;

    public TicketServiceImpl(TicketRepository repository,
                             TicketFactory ticketFactory) {
        this.repository = repository;
        this.ticketFactory = ticketFactory;
        this.commandFactory = new TicketCommandFactory(repository);
    }

    @Override
    @Transactional
    public Ticket createTicket(String eventId, TicketType type, double price, int quota) {
        Ticket ticket = ticketFactory.createTicket(eventId, type, price, quota);
        TicketCommand command = commandFactory.createTicketCommand(ticket);
        command.execute();
        return ticket;
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket viewTicket(String ticketId) {
        ViewTicketCommand command = commandFactory.viewTicketCommand(ticketId);
        command.execute();
        return command.getResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> viewTicketsByEvent(String eventId) {
        ViewEventTicketsCommand command = commandFactory.viewEventTicketsCommand(eventId);
        command.execute();
        return command.getResult();
    }

    @Override
    @Transactional
    public void updateTicket(String ticketId, Map<String, Object> updates) {
        TicketCommand command = commandFactory.updateTicketCommand(ticketId, updates);
        command.execute();
    }

    @Override
    @Transactional
    public void deleteTicket(String ticketId) {
        TicketCommand command = commandFactory.deleteTicketCommand(ticketId);
        command.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void decreaseQuota(String eventId, String ticketType, int quantity) {
        TicketType type = TicketType.valueOf(ticketType.toUpperCase());
        TicketCommand command = commandFactory.decreaseQuotaCommand(eventId, type, quantity);
        command.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userHasTicket(String userId, String eventId) {
        return true;
    }

    @Override
    @Transactional
    public void decreaseQuotaBatch(String eventId, Map<String, String> ticketQuantities) {
        TicketCommand command = commandFactory.decreaseQuotaBatchCommand(eventId, ticketQuantities);
        command.execute();
    }
}