package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;
import java.util.Map;
import java.util.NoSuchElementException;

public class UpdateTicketCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String ticketId;
    private final Map<String, Object> updates;

    public UpdateTicketCommand(TicketRepository repository, String ticketId, Map<String, Object> updates) {
        this.repository = repository;
        this.ticketId = ticketId;
        this.updates = updates;
    }

    @Override
    public void execute() {
        Ticket ticket = repository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket not found with id: " + ticketId));

        if (updates.containsKey("price")) {
            ticket.setPrice((Double) updates.get("price"));
        }

        if (updates.containsKey("quota")) {
            int newQuota = (Integer) updates.get("quota");

            int oldQuota = ticket.getQuota();
            ticket.setQuota(newQuota);
            int quotaDifference = newQuota - oldQuota;
            ticket.setRemaining(Math.max(0, ticket.getRemaining() + quotaDifference));
        }

        if (updates.containsKey("type")) {
            ticket.setType((TicketType) updates.get("type"));
        }

        repository.save(ticket);
    }
}
    