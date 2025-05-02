package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.List;

public class ViewEventTicketsCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String eventId;
    private List<Ticket> result;

    public ViewEventTicketsCommand(TicketRepository repository, String eventId) {
        this.repository = repository;
        this.eventId = eventId;
    }

    @Override
    public void execute() {
        result = repository.findByEventId(eventId);
    }

    public List<Ticket> getResult() {
        return result;
    }
}