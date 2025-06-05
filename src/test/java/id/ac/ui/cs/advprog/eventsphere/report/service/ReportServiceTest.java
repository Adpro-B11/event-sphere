package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Report report;
    private UUID reportId;
    private String userId;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
        userId = "user123";

        report = new Report(
                "Tiket tidak valid",
                "Tiket yang saya beli tidak bisa di-scan oleh panitia",
                ReportCategory.TICKET.name(),
                "TICKET-123",
                userId
        );

        // Set report ID through reflection (karena reportID di generate oleh JPA)
        try {
            java.lang.reflect.Field field = Report.class.getDeclaredField("reportID");
            field.setAccessible(true);
            field.set(report, reportId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateReport() {
        when(reportRepository.save(any(Report.class))).thenReturn(report);

        Report savedReport = reportService.createReport(report);

        assertNotNull(savedReport);
        assertEquals(report.getTitle(), savedReport.getTitle());
        assertEquals(report.getDescription(), savedReport.getDescription());
        assertEquals(ReportStatus.PENDING.getValue(), savedReport.getStatus());
        verify(reportRepository).save(any(Report.class));
    }

    @Test
    void testDeleteReport() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        doNothing().when(reportRepository).delete(report);

        reportService.deleteReport(reportId);

        verify(reportRepository).findById(reportId);
        verify(reportRepository).delete(report);
    }

    @Test
    void testDeleteReport_ReportNotFound() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportService.deleteReport(reportId);
        });

        assertEquals("Report not found", exception.getMessage());
        verify(reportRepository).findById(reportId);
        verify(reportRepository, never()).delete(any(Report.class));
    }

    @Test
    void testUpdateStatusReport() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        when(reportRepository.save(any(Report.class))).thenReturn(report);
        String newStatus = ReportStatus.ON_PROGRESS.name();

        Report updatedReport = reportService.updateStatusReport(reportId, newStatus);

        assertNotNull(updatedReport);
        assertEquals(ReportStatus.ON_PROGRESS.name(), updatedReport.getStatus());
        verify(reportRepository).findById(reportId);
        verify(reportRepository).save(report);
    }

    @Test
    void testUpdateStatusReport_InvalidStatus() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        String invalidStatus = "INVALID_STATUS";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportService.updateStatusReport(reportId, invalidStatus);
        });

        assertEquals("Status report not valid", exception.getMessage());
        verify(reportRepository).findById(reportId);
        verify(reportRepository, never()).save(any(Report.class));
    }

    @Test
    void testUpdateStatusReport_SameStatus() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));
        String sameStatus = ReportStatus.PENDING.name();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportService.updateStatusReport(reportId, sameStatus);
        });

        assertEquals("Status report already set to " + ReportStatus.PENDING.getValue(), exception.getMessage());
        verify(reportRepository).findById(reportId);
        verify(reportRepository, never()).save(any(Report.class));
    }

    @Test
    void testFindAllReport() {
        List<Report> reports = Arrays.asList(report, new Report());
        when(reportRepository.findAll()).thenReturn(reports);

        List<Report> result = reportService.findAllReport();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reportRepository).findAll();
    }

    @Test
    void testFindReportByUser() {
        List<Report> reports = Arrays.asList(report);
        when(reportRepository.findByCreatedBy(userId)).thenReturn(reports);

        List<Report> result = reportService.findReportByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getCreatedBy());
        verify(reportRepository).findByCreatedBy(userId);
    }

    @Test
    void testFindReportById() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        Report result = reportService.findReportById(reportId);

        assertNotNull(result);
        assertEquals(reportId, result.getReportID());
        verify(reportRepository).findById(reportId);
    }

    @Test
    void testFindReportById_ReportNotFound() {
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportService.findReportById(reportId);
        });

        assertEquals("Report not found", exception.getMessage());
        verify(reportRepository).findById(reportId);
    }

    @Test
    void testfindAllReportAsync_ShouldReturnAllReports() throws ExecutionException, InterruptedException {
        List<Report> mockReports = Arrays.asList(report, new Report());
        when(reportRepository.findAll()).thenReturn(mockReports);

        CompletableFuture<List<Report>> result = reportService.findAllReportAsync();

        List<Report> reports = result.get();
        assertEquals(2, reports.size());
        verify(reportRepository, times(1)).findAll();
    }

    @Test
    void testfindReportByUserAsync_ShouldReturnUserReports() throws ExecutionException, InterruptedException {
        List<Report> userReports = Arrays.asList(report);
        when(reportRepository.findByCreatedBy(userId)).thenReturn(userReports);

        CompletableFuture<List<Report>> result = reportService.findReportByUserAsync(userId);

        List<Report> reports = result.get();
        assertEquals(1, reports.size());
        assertEquals("user123", reports.get(0).getCreatedBy());
        verify(reportRepository, times(1)).findByCreatedBy(userId);
    }

}
