package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.NoSuchElementException;

public class DecreaseQuotaCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String ticketId;
    private final int quantity;

    public DecreaseQuotaCommand(TicketRepository repository, String ticketId, int quantity) {
        this.repository = repository;
        this.ticketId = ticketId;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        Ticket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found for id=" + ticketId));

        if (ticket.getQuota() < quantity) {
            throw new RuntimeException("Insufficient quota. Available: " + ticket.getQuota() + ", Requested: " + quantity);
        }

        ticket.setQuota(ticket.getQuota() - quantity);
        ticket.setRemaining(Math.max(0, ticket.getRemaining() - quantity));

        repository.save(ticket);
    }
}
