package id.ac.ui.cs.advprog.eventsphere.report.factory;

import id.ac.ui.cs.advprog.eventsphere.report.dto.CreateReportDTO;
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

    private CreateReportDTO validCreateReportDTO;
    private CreateReportDTO eventCreateReportDTO;
    private CreateReportDTO ticketCreateReportDTO;
    private CreateReportDTO paymentCreateReportDTO;

    @BeforeEach
    void setUp() {
        // Setup untuk DTO yang valid (kategori OTHER)
        validCreateReportDTO = new CreateReportDTO(
                "Masalah Umum",
                "Ini adalah deskripsi masalah umum",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        // Setup untuk DTO dengan kategori EVENT_ISSUE
        eventCreateReportDTO = new CreateReportDTO(
                "Masalah Event",
                "Ini adalah deskripsi masalah event",
                "EVENT_ISSUE",
                "event123",
                "path/to/attachment",
                "user123"
        );

        // Setup untuk DTO dengan kategori TICKET
        ticketCreateReportDTO = new CreateReportDTO(
                "Masalah Tiket",
                "Ini adalah deskripsi masalah tiket",
                "TICKET",
                "ticket123",
                "path/to/attachment",
                "user123"
        );

        // Setup untuk DTO dengan kategori PAYMENT
        paymentCreateReportDTO = new CreateReportDTO(
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
                validCreateReportDTO.getTitle(),
                validCreateReportDTO.getDescription(),
                validCreateReportDTO.getCategory().toUpperCase(),
                validCreateReportDTO.getCategoryReference(),
                validCreateReportDTO.getAttachmentPath(),
                validCreateReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(validCreateReportDTO);

        assertNotNull(result);
        assertEquals(validCreateReportDTO.getTitle(), result.getTitle());
        assertEquals(validCreateReportDTO.getDescription(), result.getDescription());
        assertEquals(validCreateReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(validCreateReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(validCreateReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEventIssue_thenSuccess() {
        Report mockReport = new Report(
                eventCreateReportDTO.getTitle(),
                eventCreateReportDTO.getDescription(),
                eventCreateReportDTO.getCategory().toUpperCase(),
                eventCreateReportDTO.getCategoryReference(),
                eventCreateReportDTO.getAttachmentPath(),
                eventCreateReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(eventCreateReportDTO);

        assertNotNull(result);
        assertEquals(eventCreateReportDTO.getTitle(), result.getTitle());
        assertEquals(eventCreateReportDTO.getDescription(), result.getDescription());
        assertEquals(eventCreateReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(eventCreateReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(eventCreateReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithTicketIssue_thenSuccess() {
        Report mockReport = new Report(
                ticketCreateReportDTO.getTitle(),
                ticketCreateReportDTO.getDescription(),
                ticketCreateReportDTO.getCategory().toUpperCase(),
                ticketCreateReportDTO.getCategoryReference(),
                ticketCreateReportDTO.getAttachmentPath(),
                ticketCreateReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(ticketCreateReportDTO);

        assertNotNull(result);
        assertEquals(ticketCreateReportDTO.getTitle(), result.getTitle());
        assertEquals(ticketCreateReportDTO.getDescription(), result.getDescription());
        assertEquals(ticketCreateReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(ticketCreateReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(ticketCreateReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithPaymentIssue_thenSuccess() {
        Report mockReport = new Report(
                paymentCreateReportDTO.getTitle(),
                paymentCreateReportDTO.getDescription(),
                paymentCreateReportDTO.getCategory().toUpperCase(),
                paymentCreateReportDTO.getCategoryReference(),
                paymentCreateReportDTO.getAttachmentPath(),
                paymentCreateReportDTO.getCreatedBy()
        );
        when(reportService.createReport(any(Report.class))).thenReturn(mockReport);

        Report result = reportFactory.createReport(paymentCreateReportDTO);

        assertNotNull(result);
        assertEquals(paymentCreateReportDTO.getTitle(), result.getTitle());
        assertEquals(paymentCreateReportDTO.getDescription(), result.getDescription());
        assertEquals(paymentCreateReportDTO.getCategory().toUpperCase(), result.getCategory());
        assertEquals(paymentCreateReportDTO.getCategoryReference(), result.getCategoryReference());
        assertEquals(paymentCreateReportDTO.getCreatedBy(), result.getCreatedBy());
        verify(reportService, times(1)).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEmptyTitle_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "",
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Title report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullTitle_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                null,
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Title report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithTitleTooLong_thenThrowException() {
        String longTitle = "Ini adalah judul yang terlalu panjang dan melebihi batas 35 karakter yang ditentukan";
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                longTitle,
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Title report cannot be more than 35 characters", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEmptyDescription_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "",
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Description report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullDescription_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                null,
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Description report can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithDescriptionTooLong_thenThrowException() {
        String longDescription = "Ini adalah deskripsi yang sangat panjang dan melewati batas 100 karakter yang ditentukan oleh sistem. " +
                "Deskripsi yang terlalu panjang seperti ini seharusnya memicu exception karena melewati batas.";
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                longDescription,
                "OTHER",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Description report cannot be more than 100 characters", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithInvalidCategory_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "INVALID_CATEGORY",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Category report not valid", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullCategory_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                null,
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Category report can't be null", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithEmptyCreatedBy_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                ""
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Report creator can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithNullCreatedBy_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "OTHER",
                null,
                "path/to/attachment",
                null
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Report creator can't be empty", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateOtherReportWithReference_thenThrowException() {
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "OTHER",
                "some-reference",
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("No need Reference for category: OTHER", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }

    @Test
    void whenCreateReportWithDefaultCase_thenThrowException() {
        // Arrange - Ini skenario edge case untuk branch coverage
        // mock untuk ReportCategory yang mengembalikan null untuk valueOf
        CreateReportDTO invalidCreateReportDTO = new CreateReportDTO(
                "Judul Valid",
                "Deskripsi valid",
                "UNKNOWN",
                null,
                "path/to/attachment",
                "user123"
        );

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportFactory.createReport(invalidCreateReportDTO);
        });
        assertEquals("Category report not valid", exception.getMessage());
        verify(reportService, never()).createReport(any(Report.class));
    }
}
