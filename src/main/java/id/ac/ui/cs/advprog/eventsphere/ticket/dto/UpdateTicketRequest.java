package id.ac.ui.cs.advprog.eventsphere.ticket.dto;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import lombok.Data;

import jakarta.validation.constraints.Min;

@Data
public class UpdateTicketRequest {
    private TicketType type;

    @Min(value = 0, message = "Price must be non-negative")
    private Double price;

    @Min(value = 1, message = "Quota must be at least 1")
    private Integer quota;
}