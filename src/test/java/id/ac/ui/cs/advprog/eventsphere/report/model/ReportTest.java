package id.ac.ui.cs.advprog.eventsphere.report.model;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    private Report report;
    private String reportId;
    private String attendeeId;
    private String eventId;
    private String ticketId;
    private String description;
    private String category;
    private ReportStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> attachments;
    private String responseMessage;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID().toString();
        attendeeId = UUID.randomUUID().toString();
        eventId = UUID.randomUUID().toString();
        ticketId = UUID.randomUUID().toString();
        description = "Test description for report";
        category = ReportCategory.TICKET.getValue();
        status = ReportStatus.ON_PROGRESS;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        attachments = Arrays.asList("url1", "url2");
        responseMessage = "Response message for test";

        report = new Report(
                reportId,
                attendeeId,
                eventId,
                ticketId,
                description,
                category,
                status,
                createdAt,
                updatedAt,
                attachments,
                responseMessage
        );
    }

    @Test
    void shouldCreateReportWithAllFields() {
        assertEquals(reportId, report.getReportId());
        assertEquals(attendeeId, report.getAttendeeId());
        assertEquals(eventId, report.getEventId());
        assertEquals(ticketId, report.getTicketId());
        assertEquals(description, report.getDescription());
        assertEquals(category, report.getCategory());
        assertEquals(status, report.getStatus());
        assertEquals(createdAt, report.getCreatedAt());
        assertEquals(updatedAt, report.getUpdatedAt());
        assertEquals(attachments, report.getAttachments());
        assertEquals(responseMessage, report.getResponseMessage());
    }

    @Test
    void shouldCreateReportWithRequiredFieldsOnly() {
        Report minimalReport = new Report(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                null,
                null,
                "Minimal report description",
                ReportCategory.PAYMENT.getValue(),
                ReportStatus.ON_PROGRESS,
                LocalDateTime.now(),
                null,
                null,
                null
        );

        assertNotNull(minimalReport.getReportId());
        assertNotNull(minimalReport.getAttendeeId());
        assertNull(minimalReport.getEventId());
        assertNull(minimalReport.getTicketId());
        assertEquals("Minimal report description", minimalReport.getDescription());
        assertEquals(ReportCategory.PAYMENT.getValue(), minimalReport.getCategory());
        assertEquals(ReportStatus.ON_PROGRESS, minimalReport.getStatus());
        assertNotNull(minimalReport.getCreatedAt());
        assertNull(minimalReport.getUpdatedAt());
        assertNull(minimalReport.getAttachments());
        assertNull(minimalReport.getResponseMessage());
    }

    @Test
    void shouldUpdateStatus() {
        report.setStatus(ReportStatus.RESOLVED);
        assertEquals(ReportStatus.RESOLVED, report.getStatus());
    }

    @Test
    void shouldUpdateResponseMessage() {
        String newMessage = "Updated response message";
        report.setResponseMessage(newMessage);
        assertEquals(newMessage, report.getResponseMessage());
    }

    @Test
    void shouldUpdateAttachments() {
        List<String> newAttachments = Arrays.asList("new-url1", "new-url2", "new-url3");
        report.setAttachments(newAttachments);
        assertEquals(newAttachments, report.getAttachments());
    }

    @Test
    void shouldHandleNullAttachments() {
        report.setAttachments(null);
        assertNull(report.getAttachments());
    }

    @Test
    void shouldThrowExceptionForInvalidStatus() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> report.setStatus(null)
        );
        assertEquals("Report status cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidCategory() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> report.setCategory("")
        );
        assertEquals("Report category cannot be null or invalid", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidAttendeeId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Report(
                        UUID.randomUUID().toString(),
                        null,
                        eventId,
                        ticketId,
                        description,
                        category,
                        status,
                        createdAt,
                        updatedAt,
                        attachments,
                        responseMessage
                )
        );
        assertEquals("Attendee ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForInvalidDescription() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Report(
                        UUID.randomUUID().toString(),
                        attendeeId,
                        eventId,
                        ticketId,
                        "",
                        ReportCategory.TICKET.getValue(),
                        ReportStatus.ON_PROGRESS,
                        createdAt,
                        updatedAt,
                        attachments,
                        responseMessage
                )
        );
        assertEquals("Description cannot be null or empty", exception.getMessage());
    }
}
