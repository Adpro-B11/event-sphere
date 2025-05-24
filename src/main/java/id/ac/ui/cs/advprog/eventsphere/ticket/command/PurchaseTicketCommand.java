package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.NoSuchElementException;

public class PurchaseTicketCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String ticketId;
    private final int quantity;

    public PurchaseTicketCommand(TicketRepository repository, String ticketId, int quantity) {
        this.repository = repository;
        this.ticketId = ticketId;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        Ticket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found with id: " + ticketId));

        if (ticket == null) {
            throw new IllegalArgumentException("Ticket not found");
        }

        if (!ticket.isActive()) {
            throw new IllegalStateException("Ticket is not available for purchase");
        }

        if (ticket.getRemaining() < quantity) {
            throw new IllegalStateException("Insufficient tickets available. Only " +
                    ticket.getRemaining() + " tickets remaining");
        }

        ticket.setRemaining(ticket.getRemaining() - quantity);
        repository.save(ticket);
    }
}
