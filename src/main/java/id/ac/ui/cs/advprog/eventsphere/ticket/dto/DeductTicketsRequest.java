package id.ac.ui.cs.advprog.eventsphere.ticket.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DeductTicketsRequest {
    private Map<String, String> tickets;
    private String eventId;
}
