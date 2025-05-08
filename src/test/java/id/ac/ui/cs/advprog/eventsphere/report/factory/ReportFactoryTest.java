package id.ac.ui.cs.advprog.eventsphere.report.factory;

import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportDTO;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportFactoryTest {

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportFactory reportFactory;

    private ReportDTO validReportDTO;
    private ReportDTO eventReportDTO;
    private ReportDTO ticketReportDTO;
    private ReportDTO paymentReportDTO;

    @BeforeEach
    void setUp() {
        // Setup untuk DTO yang valid (kategori OTHER)
        validReportDTO = new ReportDTO(
                "Masalah Umum",
                "Ini adalah deskripsi masalah umum",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        // Setup untuk DTO dengan kategori EVENT_ISSUE
        eventReportDTO = new ReportDTO(
                "Masalah Event",
                "Ini adalah deskripsi masalah event",
                "EVENT_ISSUE",
                "event123",
                "path/to/attachment",
                "user123"
        );

        // Setup untuk DTO dengan kategori TICKET
        ticketReportDTO = new ReportDTO(
                "Masalah Tiket",
                "Ini adalah deskripsi masalah tiket",
                "TICKET",
                "ticket123",
                "path/to/attachment",
                "user123"
        );

        // Setup untuk DTO dengan kategori PAYMENT
        paymentReportDTO = new ReportDTO(
                "Masalah Pembayaran",
                "Ini adalah deskripsi masalah pembayaran",
                "PAYMENT",
                "payment123",
                "path/to/attachment",
                "user123"
        );
    }

    @Test
    void whenCreateReportWithValidData_thenSuccess() {
        Report mockReport = new Report(
                validReportDTO.getTitle(),
                validReportDTO.getDescription(),
                validReportDTO.getCategory().toUpperCase(),
                validReportDTO.getCategoryReference(),
                validReportDTO.getAttachmentPath(),
                validReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(validReportDTO);

        assertNotNull(result);
        assertEquals(validReportDTO.getTitle(), result.getTitle());
        assertEquals(validReportDTO.getDescription(), result.getDescription());
        assertEquals(validReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(validReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(validReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEventIssue_thenSuccess() {
        Report mockReport = new Report(
                eventReportDTO.getTitle(),
                eventReportDTO.getDescription(),
                eventReportDTO.getCategory().toUpperCase(),
                eventReportDTO.getCategoryReference(),
                eventReportDTO.getAttachmentPath(),
                eventReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(eventReportDTO);

        assertNotNull(result);
        assertEquals(eventReportDTO.getTitle(), result.getTitle());
        assertEquals(eventReportDTO.getDescription(), result.getDescription());
        assertEquals(eventReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(eventReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(eventReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithTicketIssue_thenSuccess() {
        Report mockReport = new Report(
                ticketReportDTO.getTitle(),
                ticketReportDTO.getDescription(),
                ticketReportDTO.getCategory().toUpperCase(),
                ticketReportDTO.getCategoryReference(),
                ticketReportDTO.getAttachmentPath(),
                ticketReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(ticketReportDTO);

        assertNotNull(result);
        assertEquals(ticketReportDTO.getTitle(), result.getTitle());
        assertEquals(ticketReportDTO.getDescription(), result.getDescription());
        assertEquals(ticketReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(ticketReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(ticketReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithPaymentIssue_thenSuccess() {
        Report mockReport = new Report(
                paymentReportDTO.getTitle(),
                paymentReportDTO.getDescription(),
                paymentReportDTO.getCategory().toUpperCase(),
                paymentReportDTO.getCategoryReference(),
                paymentReportDTO.getAttachmentPath(),
                paymentReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(paymentReportDTO);

        assertNotNull(result);
        assertEquals(paymentReportDTO.getTitle(), result.getTitle());
        assertEquals(paymentReportDTO.getDescription(), result.getDescription());
        assertEquals(paymentReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(paymentReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(paymentReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEmptyTitle_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "",
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Title report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullTitle_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                null,
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Title report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithTitleTooLong_thenThrowException() {
        String longTitle = "Ini adalah judul yang terlalu panjang dan melebihi batas 35 karakter yang ditentukan";
        ReportDTO invalidReportDTO = new ReportDTO(
                longTitle,
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Title report cannot be more than 35 characters", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEmptyDescription_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Description report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullDescription_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                null,
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Description report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithDescriptionTooLong_thenThrowException() {
        String longDescription = "Ini adalah deskripsi yang sangat panjang dan melewati batas 100 karakter yang ditentukan oleh sistem. " +
                "Deskripsi yang terlalu panjang seperti ini seharusnya memicu exception karena melewati batas.";
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                longDescription,
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Description report cannot be more than 100 characters", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithInvalidCategory_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "INVALID_CATEGORY",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Category report not valid", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullCategory_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                null,
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Category report can't be null", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEmptyCreatedBy_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                ""
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Report creator can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullCreatedBy_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                null
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Report creator can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateOtherReportWithReference_thenThrowException() {
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "OTHER",
                "some-reference",
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("No need Reference for category: OTHER", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithDefaultCase_thenThrowException() {
        // Arrange - Ini skenario edge case untuk branch coverage
        // mock untuk ReportCategory yang mengembalikan null untuk valueOf
        ReportDTO invalidReportDTO = new ReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "UNKNOWN",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidReportDTO);
        });
        assertEquals("Category report not valid", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }
}
