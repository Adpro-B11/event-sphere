package id.ac.ui.cs.advprog.eventsphere.ticket.dto;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateTicketRequest {
    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotNull(message = "Ticket type is required")
    private TicketType type;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @NotNull(message = "Quota is required")
    @Min(value = 1, message = "Quota must be at least 1")
    private Integer quota;
}