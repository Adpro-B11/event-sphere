package id.ac.ui.cs.advprog.eventsphere.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ReportMessageDTO {
    private UUID reportID;
    private String sender;
    private String message;
}