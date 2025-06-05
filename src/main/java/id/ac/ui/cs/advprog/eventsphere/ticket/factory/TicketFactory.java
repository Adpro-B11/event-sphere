package id.ac.ui.cs.advprog.eventsphere.ticket.factory;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketFactory {

    public Ticket createTicket(String eventId, TicketType type, double price, int quota) {
        Ticket ticket = new Ticket();
        ticket.setEventId(eventId);
        ticket.setType(type);
        ticket.setPrice(price);
        ticket.setQuota(quota);
        ticket.setRemaining(quota);

        applyTypeSpecificRules(ticket);

        return ticket;
    }

    private void applyTypeSpecificRules(Ticket ticket) {
        switch (ticket.getType()) {
            case EARLY_BIRD:
                if (ticket.getQuota() > 100) {
                    ticket.setQuota(100);
                    ticket.setRemaining(100);
                }
                break;
            default:
                break;
        }
    }
}