package id.ac.ui.cs.advprog.eventsphere.report.mapper;

import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportDetailDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportListDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for conversion between Report entity and DTO
 */
@Component
public class ReportMapper {

    private final ReportMessageMapper reportMessageMapper;

    @Autowired
    public ReportMapper(ReportMessageMapper reportMessageMapper) {
        this.reportMessageMapper = reportMessageMapper;
    }

     /**
     * Convert Report entity to ReportListDTO
     */
    public ReportListDTO toReportListDTO(Report report) {
        if (report == null) {
            return null;
        }

        return ReportListDTO.builder()
                .reportID(report.getReportID())
                .title(report.getTitle())
                .description(report.getDescription())
                .category(report.getCategory())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .createdBy(report.getCreatedBy())
                .build();
    }

    /**
     * Convert Report entity to ReportDetailDTO
     */
    public ReportDetailDTO toReportDetailDTO(Report report) {
        if (report == null) {
            return null;
        }

        List<ReportMessageDTO> messageDTOs = report.getMessages() != null
                ? reportMessageMapper.toReportMessageDTOs(report.getMessages())
                : Collections.emptyList();

        return ReportDetailDTO.builder()
                .reportID(report.getReportID())
                .title(report.getTitle())
                .description(report.getDescription())
                .category(report.getCategory())
                .categoryReference(report.getCategoryReference())
                .attachmentPath(report.getAttachmentPath())
                .status(report.getStatus())
                .createdAt(report.getCreatedAt())
                .createdBy(report.getCreatedBy())
                .messages(messageDTOs)
                .build();
    }

    /**
     * Convert list of Report entities to list of ReportListDTO
     */
    public List<ReportListDTO> toReportListDTOs(List<Report> reports) {
        if (reports == null) {
            return Collections.emptyList();
        }

        return reports.stream()
                .map(this::toReportListDTO)
                .collect(Collectors.toList());
    }
}
