package id.ac.ui.cs.advprog.eventsphere.report.controller;

import id.ac.ui.cs.advprog.eventsphere.report.dto.CreateReportDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportDetailDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportListDTO;
import id.ac.ui.cs.advprog.eventsphere.report.factory.ReportFactory;
import id.ac.ui.cs.advprog.eventsphere.report.mapper.ReportMapper;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @Mock
    private ReportFactory reportFactory;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;
    private Report report;
    private ReportDetailDTO reportDetailDTO;
    private ReportListDTO reportListDTO;
    private UUID reportId;
    private String userId;
    private List<Report> reportList;
    private List<ReportListDTO> reportListDTOs;
    private CreateReportDTO createReportDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();

        reportId = UUID.randomUUID();
        userId = "user123";

        report = new Report(
                "Tiket tidak muncul",
                "Tiket tidak muncul setelah pembayaran",
                "TICKET",
                "TICKET-123",
                userId
        );

        // Refleksi untuk mengatur reportID karena field tersebut final dan tidak dapat diubah langsung
        try {
            java.lang.reflect.Field reportIDField = Report.class.getDeclaredField("reportID");
            reportIDField.setAccessible(true);
            reportIDField.set(report, reportId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        reportDetailDTO = ReportDetailDTO.builder()
                .reportID(reportId)
                .title("Tiket tidak muncul")
                .description("Tiket tidak muncul setelah pembayaran")
                .category("TICKET")
                .categoryReference("TICKET-123")
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .createdBy(userId)
                .messages(Collections.emptyList())
                .build();

        reportListDTO = ReportListDTO.builder()
                .reportID(reportId)
                .title("Tiket tidak muncul")
                .description("Tiket tidak muncul setelah pembayaran")
                .category("TICKET")
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .createdBy(userId)
                .build();

        reportList = Collections.singletonList(report);
        reportListDTOs = Collections.singletonList(reportListDTO);

        createReportDTO = CreateReportDTO.builder()
                .title("Tiket tidak muncul")
                .description("Tiket tidak muncul setelah pembayaran")
                .category("TICKET")
                .categoryReference("TICKET-123")
                .createdBy(userId)
                .build();
    }

    @Test
    void createReport_ShouldReturnCreatedReport() {
        when(reportFactory.createReport(any(CreateReportDTO.class))).thenReturn(report);

        ResponseEntity<Report> response = reportController.createReport(createReportDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(report, response.getBody());
        verify(reportFactory, times(1)).createReport(createReportDTO);
    }

    @Test
    void updateStatusReport_WhenStatusExists_ShouldReturnUpdatedReport() {
        Map<String, String> statusUpdate = new HashMap<>();
        String newStatus = "ON PROGRESS";
        statusUpdate.put("status", newStatus);

        when(reportService.updateStatusReport(reportId, newStatus)).thenReturn(report);
        when(reportMapper.toReportDetailDTO(report)).thenReturn(reportDetailDTO);

        ResponseEntity<ReportDetailDTO> response = reportController.updateStatusReport(reportId, statusUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportDetailDTO, response.getBody());
        verify(reportService, times(1)).updateStatusReport(reportId, newStatus);
        verify(reportMapper, times(1)).toReportDetailDTO(report);
    }

    @Test
    void updateStatusReport_WhenStatusDoesNotExist_ShouldReturnBadRequest() {
        Map<String, String> statusUpdate = new HashMap<>();

        ResponseEntity<ReportDetailDTO> response = reportController.updateStatusReport(reportId, statusUpdate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(reportService, never()).updateStatusReport(any(), any());
    }

    @Test
    void deleteReport_ShouldReturnSuccessMessage() {
        doNothing().when(reportService).deleteReport(reportId);

        ResponseEntity<String> response = reportController.deleteReport(reportId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Report deleted successfully", response.getBody());
        verify(reportService, times(1)).deleteReport(reportId);
    }

    @Test
    void getAllReports_ShouldReturnAllReports() {
        when(reportService.findAllReport()).thenReturn(reportList);
        when(reportMapper.toReportListDTOs(reportList)).thenReturn(reportListDTOs);

        ResponseEntity<List<ReportListDTO>> response = reportController.getAllReports();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportListDTOs, response.getBody());
        verify(reportService, times(1)).findAllReport();
        verify(reportMapper, times(1)).toReportListDTOs(reportList);
    }

    @Test
    void getReportByUser_ShouldReturnUserReports() {
        when(reportService.findReportByUser(userId)).thenReturn(reportList);
        when(reportMapper.toReportListDTOs(reportList)).thenReturn(reportListDTOs);

        ResponseEntity<List<ReportListDTO>> response = reportController.getReportByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportListDTOs, response.getBody());
        verify(reportService, times(1)).findReportByUser(userId);
        verify(reportMapper, times(1)).toReportListDTOs(reportList);
    }

    @Test
    void getReportById_ShouldReturnReport() {
        when(reportService.findReportById(reportId)).thenReturn(report);
        when(reportMapper.toReportDetailDTO(report)).thenReturn(reportDetailDTO);

        ResponseEntity<ReportDetailDTO> response = reportController.getReportById(reportId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportDetailDTO, response.getBody());
        verify(reportService, times(1)).findReportById(reportId);
        verify(reportMapper, times(1)).toReportDetailDTO(report);
    }

    @Test
    void getAllReportsAsync_ShouldReturnAllReports() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Report>> completableFuture = CompletableFuture.completedFuture(reportList);
        when(reportService.findAllReportAsync()).thenReturn(completableFuture);
        when(reportMapper.toReportListDTOs(reportList)).thenReturn(reportListDTOs);

        CompletableFuture<ResponseEntity<List<ReportListDTO>>> response = reportController.getAllReportsAsync();

        ResponseEntity<List<ReportListDTO>> result = response.get();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(reportListDTOs, result.getBody());
        verify(reportService, times(1)).findAllReportAsync();
        verify(reportMapper, times(1)).toReportListDTOs(reportList);
    }

    @Test
    void getReportByUserAsync_ShouldReturnUserReports() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Report>> completableFuture = CompletableFuture.completedFuture(reportList);
        when(reportService.findReportByUserAsync(userId)).thenReturn(completableFuture);
        when(reportMapper.toReportListDTOs(reportList)).thenReturn(reportListDTOs);

        CompletableFuture<ResponseEntity<List<ReportListDTO>>> response = reportController.getReportByUserAsync(userId);

        ResponseEntity<List<ReportListDTO>> result = response.get();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(reportListDTOs, result.getBody());
        verify(reportService, times(1)).findReportByUserAsync(userId);
        verify(reportMapper, times(1)).toReportListDTOs(reportList);
    }
}
