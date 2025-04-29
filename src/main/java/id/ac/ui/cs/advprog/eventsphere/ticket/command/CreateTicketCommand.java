package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

public class CreateTicketCommand implements TicketCommand {
    private final TicketRepository repository;
    private final Ticket ticket;

    public CreateTicketCommand(TicketRepository repository, Ticket ticket) {
        this.repository = repository;
        this.ticket = ticket;
    }

    @Override
    public void execute() {
        repository.save(ticket);
    }
}