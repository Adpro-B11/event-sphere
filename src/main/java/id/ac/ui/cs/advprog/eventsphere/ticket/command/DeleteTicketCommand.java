package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

public class DeleteTicketCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String ticketId;

    public DeleteTicketCommand(TicketRepository repository, String ticketId) {
        this.repository = repository;
        this.ticketId = ticketId;
    }

    @Override
    public void execute() {
        repository.deleteById(ticketId);
    }
}
    