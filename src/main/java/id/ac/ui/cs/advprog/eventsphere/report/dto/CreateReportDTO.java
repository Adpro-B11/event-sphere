package id.ac.ui.cs.advprog.eventsphere.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportDTO {
    private String title;
    private String description;
    private String category;
    private String categoryReference;
    private String createdBy;
}