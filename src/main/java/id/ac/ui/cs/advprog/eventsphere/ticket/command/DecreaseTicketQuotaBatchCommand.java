package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.Map;

public class DecreaseTicketQuotaBatchCommand implements TicketCommand {
    private final TicketRepository repository;
    private final String eventId;
    private final Map<String, String> ticketQuantities;

    public DecreaseTicketQuotaBatchCommand(TicketRepository repository, String eventId, Map<String, String> ticketQuantities) {
        this.repository = repository;
        this.eventId = eventId;
        this.ticketQuantities = ticketQuantities;
    }

    @Override
    public void execute() {
        for (Map.Entry<String, String> entry : ticketQuantities.entrySet()) {
            String typeStr = entry.getKey();
            int qty = Integer.parseInt(entry.getValue());
            TicketType type = TicketType.valueOf(typeStr);
            Ticket ticket = repository.findByEventIdAndType(eventId, type)
                    .orElseThrow(() -> new IllegalArgumentException("Ticket not found for type " + typeStr + " in event " + eventId));
            int newRemaining = ticket.getRemaining() - qty;
            if (newRemaining < 0) {
                throw new IllegalArgumentException("Insufficient quota for ticket type " + typeStr);
            }
            ticket.setRemaining(newRemaining);
            repository.save(ticket);
        }
    }

}
