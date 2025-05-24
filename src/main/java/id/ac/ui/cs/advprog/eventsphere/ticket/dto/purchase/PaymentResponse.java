package id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String transactionId;
    private String status;
    private String message;
    private double amount;
    private String paymentMethod;
    private String transactionDate;
}