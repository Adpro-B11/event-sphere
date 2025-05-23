package id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String userId;
    private String ticketId;
    private String eventId;
    private double amount;
    private int quantity;
    private String paymentMethod;
}