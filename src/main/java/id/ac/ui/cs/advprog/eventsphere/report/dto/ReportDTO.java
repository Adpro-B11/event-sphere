package id.ac.ui.cs.advprog.eventsphere.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDTO {
    private String title;
    private String description;
    private String category;
    private String categoryReference;
    private String attachmentPath;
    private String createdBy;
}