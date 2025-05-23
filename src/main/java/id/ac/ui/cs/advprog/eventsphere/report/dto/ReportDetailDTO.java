package id.ac.ui.cs.advprog.eventsphere.report.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailDTO {
    private UUID reportID;
    private String title;
    private String description;
    private String category;
    private String categoryReference;
    private String attachmentPath;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private List<ReportMessageDTO> messages;
}