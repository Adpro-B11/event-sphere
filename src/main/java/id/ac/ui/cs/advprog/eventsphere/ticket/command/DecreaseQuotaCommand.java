package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class DecreaseQuotaCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String eventId;
    private final TicketType ticketType;
    private final int quantity;

    public DecreaseQuotaCommand(TicketRepository repository, String eventId, TicketType ticketType, int quantity) {
        this.repository = repository;
        this.eventId = eventId;
        this.ticketType = ticketType;
        this.quantity = quantity;
    }

    @Override
    public void execute() {
        List<Ticket> eventTickets = repository.findByEventId(eventId);

        Ticket ticket = eventTickets.stream()
                .filter(t -> t.getType() == ticketType)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "Ticket not found for eventId=" + eventId + " and type=" + ticketType));

        if (ticket.getQuota() < quantity) {
            throw new RuntimeException("Insufficient quota. Available: " + ticket.getQuota() + ", Requested: " + quantity);
        }

        ticket.setQuota(ticket.getQuota() - quantity);
        ticket.setRemaining(Math.max(0, ticket.getRemaining() - quantity));

        repository.save(ticket);
    }
}
