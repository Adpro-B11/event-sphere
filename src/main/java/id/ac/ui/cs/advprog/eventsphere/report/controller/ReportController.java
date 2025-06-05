package id.ac.ui.cs.advprog.eventsphere.report.controller;


import id.ac.ui.cs.advprog.eventsphere.report.dto.CreateReportDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportDetailDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportListDTO;
import id.ac.ui.cs.advprog.eventsphere.report.factory.ReportFactory;
import id.ac.ui.cs.advprog.eventsphere.report.mapper.ReportMapper;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller responsible for handling report-related API endpoints.
 * Manages CRUD operations for reports and provides endpoints for retrieving reports by various criteria.
 */
@RestController
@RequestMapping("api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportFactory reportFactory;
    private final ReportMapper reportMapper;

    /**
     * Creates a new report using the provided report data.
     *
     * @param createReportDTO Data transfer object containing report information
     * @return The newly created report with HTTP status 201 (Created)
     */
    @PostMapping("/new")
    public ResponseEntity<Report> createReport(@RequestBody CreateReportDTO createReportDTO) {
        Report report = reportFactory.createReport(createReportDTO);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    /**
     * Updates the status of an existing report.
     *
     * @param reportId ID of the report to update
     * @param statusUpdate Map containing the new status value
     * @return The updated report or HTTP 400 if status is missing
     */
    @PatchMapping("/{reportId}/status")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<ReportDetailDTO> updateStatusReport(@PathVariable("reportId") UUID reportId,
                                                     @RequestBody Map<String, String> statusUpdate) {

        String newStatus = statusUpdate.get("status");
        if (newStatus == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Report updatedReport = reportService.updateStatusReport(reportId, newStatus);
        return ResponseEntity.ok(reportMapper.toReportDetailDTO(updatedReport));
    }

    /**
     * Deletes a report by its ID.
     *
     * @param reportId ID of the report to delete
     * @return Confirmation message upon successful deletion
     */
    @DeleteMapping("/{reportId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<String> deleteReport(@PathVariable("reportId") UUID reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok("Report deleted successfully");
    }

    /**
     * Retrieves all reports in the system.
     *
     * @return List of all reports
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<List<ReportListDTO>> getAllReports() {
        List<Report> reports = reportService.findAllReport();
        return ResponseEntity.ok(reportMapper.toReportListDTOs(reports));
    }

    /**
     * Retrieves all reports submitted by a specific user.
     *
     * @param userId ID of the user whose reports to find
     * @return List of reports associated with the specified user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReportListDTO>> getReportByUser(@PathVariable("userId") String userId) {
        List<Report> reports = reportService.findReportByUser(userId);
        return ResponseEntity.ok(reportMapper.toReportListDTOs(reports));
    }

    /**
     * Retrieves a specific report by its ID.
     *
     * @param reportId ID of the report to retrieve
     * @return The requested report
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailDTO> getReportById(@PathVariable("reportId") UUID reportId) {
        Report report = reportService.findReportById(reportId);
        return ResponseEntity.ok(reportMapper.toReportDetailDTO(report));
    }

    /**
     * Retrieves all reports asynchronously.
     *
     * @return List of all reports
     */
    @GetMapping("/async")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public CompletableFuture<ResponseEntity<List<ReportListDTO>>> getAllReportsAsync() {
        return reportService.findAllReportAsync()
                .thenApply(reports -> ResponseEntity.ok(reportMapper.toReportListDTOs(reports)))
                .exceptionally(ex -> {
                    // Handle error jika terjadi exception
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    /**
     * Retrieves all reports submitted by a specific user asynchronously.
     *
     * @param userId ID of the user whose reports to find
     * @return List of reports associated with the specified user
     */
    @GetMapping("/user/{userId}/async")
    public CompletableFuture<ResponseEntity<List<ReportListDTO>>> getReportByUserAsync(@PathVariable("userId") String userId) {
        return reportService.findReportByUserAsync(userId)
                .thenApply(reports -> ResponseEntity.ok(reportMapper.toReportListDTOs(reports)))
                .exceptionally(ex -> {
                    // Handle error jika terjadi exception
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

}