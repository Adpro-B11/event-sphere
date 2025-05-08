package id.ac.ui.cs.advprog.eventsphere.report.dto.reportmessage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateReportMessageDTO {
    private UUID reportID;
    private String sender;
    private String message;
}