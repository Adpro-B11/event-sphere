package id.ac.ui.cs.advprog.eventsphere.report.dto.message;

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