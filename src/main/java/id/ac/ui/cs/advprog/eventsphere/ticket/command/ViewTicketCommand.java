package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

public class ViewTicketCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String ticketId;
    private Ticket result;

    public ViewTicketCommand(TicketRepository repository, String ticketId) {
        this.repository = repository;
        this.ticketId = ticketId;
    }

    @Override
    public void execute() {
        result = repository.findById(ticketId);
    }

    public Ticket getResult() {
        return result;
    }
}
