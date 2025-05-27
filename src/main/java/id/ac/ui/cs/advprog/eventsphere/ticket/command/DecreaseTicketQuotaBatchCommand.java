package id.ac.ui.cs.advprog.eventsphere.ticket.command;

import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import id.ac.ui.cs.advprog.eventsphere.ticket.repository.TicketRepository;

import java.util.Map;

public class DecreaseTicketQuotaBatchCommand implements TicketCommand {
    private final TicketRepository repository;
    private final Map<String, String> ticketQuantities;

    public DecreaseTicketQuotaBatchCommand(TicketRepository repository, Map<String, String> ticketQuantities) {
        this.repository = repository;
        this.ticketQuantities = ticketQuantities;
    }

    @Override
    public void execute() {
        for (Map.Entry<String, String> entry : ticketQuantities.entrySet()) {
            String ticketId = entry.getKey();
            int qty = Integer.parseInt(entry.getValue());
            Ticket ticket = repository.findById(ticketId)
                    .orElseThrow(() -> new IllegalArgumentException("Ticket not found for id " + ticketId));

            int newRemaining = ticket.getRemaining() - qty;
            if (newRemaining < 0) {
                throw new IllegalArgumentException("Insufficient quota for ticket id " + ticketId);
            }
            ticket.setRemaining(newRemaining);
            repository.save(ticket);
        }
    }
}
