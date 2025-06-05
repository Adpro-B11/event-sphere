package id.ac.ui.cs.advprog.eventsphere.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportMessageDTO {
    private UUID messageID;
    private String message;
    private LocalDateTime timestamp;
    private String sender;
}

