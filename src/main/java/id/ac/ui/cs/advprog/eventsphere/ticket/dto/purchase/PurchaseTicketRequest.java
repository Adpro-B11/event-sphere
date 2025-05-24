package id.ac.ui.cs.advprog.eventsphere.ticket.dto.purchase;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class PurchaseTicketRequest {
    @NotBlank(message = "Ticket ID is required")
    private String ticketId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
}