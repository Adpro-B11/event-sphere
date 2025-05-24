package id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase;

import id.ac.ui.cs.advprog.eventsphere.ticket.dto.TicketResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseTicketResponse {
    private String transactionId;
    private String ticketId;
    private String eventId;
    private int quantity;
    private double totalAmount;
    private String paymentStatus;
    private String paymentMethod;
    private TicketResponse ticketDetails;
}