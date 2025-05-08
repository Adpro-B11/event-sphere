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
public class ReportListDTO {
    private UUID reportID;
    private String title;
    private String description;
    private String category;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
}