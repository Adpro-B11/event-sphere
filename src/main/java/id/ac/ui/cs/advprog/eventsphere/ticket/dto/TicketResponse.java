package id.ac.ui.cs.advprog.eventsphere.ticket.dto;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import id.ac.ui.cs.advprog.eventsphere.ticket.model.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private String id;
    private String eventId;
    private TicketType type;
    private double price;
    private int quota;
    private int remaining;
    private boolean active;

    public static TicketResponse fromTicket(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .eventId(ticket.getEventId())
                .type(ticket.getType())
                .price(ticket.getPrice())
                .quota(ticket.getQuota())
                .remaining(ticket.getRemaining())
                .active(ticket.isActive())
                .build();
    }
}