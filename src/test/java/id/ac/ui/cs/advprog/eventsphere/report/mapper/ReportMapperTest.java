package id.ac.ui.cs.advprog.eventsphere.report.mapper;

import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportDetailDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportListDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportMapperTest {

    @Mock
    private ReportMessageMapper reportMessageMapper;

    @InjectMocks
    private ReportMapper reportMapper;

    private Report testReport;
    private UUID reportId;
    private LocalDateTime createdAt;
    private List<ReportMessage> messages;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
        createdAt = LocalDateTime.now();

        // Setup test report
        testReport = new Report();
        try {
            java.lang.reflect.Field idField = Report.class.getDeclaredField("reportID");
            idField.setAccessible(true);
            idField.set(testReport, reportId);

            java.lang.reflect.Field titleField = Report.class.getDeclaredField("title");
            titleField.setAccessible(true);
            titleField.set(testReport, "Test Report");

            java.lang.reflect.Field descField = Report.class.getDeclaredField("description");
            descField.setAccessible(true);
            descField.set(testReport, "Test Description");

            java.lang.reflect.Field categoryField = Report.class.getDeclaredField("category");
            categoryField.setAccessible(true);
            categoryField.set(testReport, "EVENT_ISSUE");

            java.lang.reflect.Field refField = Report.class.getDeclaredField("categoryReference");
            refField.setAccessible(true);
            refField.set(testReport, "event-123");

            java.lang.reflect.Field attachField = Report.class.getDeclaredField("attachmentPath");
            attachField.setAccessible(true);
            attachField.set(testReport, "/path/to/file.jpg");

            java.lang.reflect.Field statusField = Report.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(testReport, ReportStatus.PENDING.getValue());

            java.lang.reflect.Field dateField = Report.class.getDeclaredField("createdAt");
            dateField.setAccessible(true);
            dateField.set(testReport, createdAt);

            java.lang.reflect.Field createdByField = Report.class.getDeclaredField("createdBy");
            createdByField.setAccessible(true);
            createdByField.set(testReport, "user-123");

            // Setup messages
            ReportMessage message1 = new ReportMessage();
            ReportMessage message2 = new ReportMessage();
            messages = Arrays.asList(message1, message2);

            java.lang.reflect.Field messagesField = Report.class.getDeclaredField("messages");
            messagesField.setAccessible(true);
            messagesField.set(testReport, messages);
        } catch (Exception e) {
            fail("Failed to set up test data: " + e.getMessage());
        }
    }

    @Test
    void toReportListDTO_ShouldMapAllFields() {
        ReportListDTO result = reportMapper.toReportListDTO(testReport);

        assertNotNull(result);
        assertEquals(reportId, result.getReportID());
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("EVENT_ISSUE", result.getCategory());
        assertEquals(ReportStatus.PENDING.getValue(), result.getStatus());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals("user-123", result.getCreatedBy());
    }

    @Test
    void toReportListDTO_ShouldReturnNull_WhenReportIsNull() {
        ReportListDTO result = reportMapper.toReportListDTO(null);

        assertNull(result);
    }

    @Test
    void toReportDetailDTO_ShouldMapAllFields() {
        List<ReportMessageDTO> messageDTOs = Arrays.asList(
                ReportMessageDTO.builder().messageID(UUID.randomUUID()).build(),
                ReportMessageDTO.builder().messageID(UUID.randomUUID()).build()
        );
        when(reportMessageMapper.toReportMessageDTOs(messages)).thenReturn(messageDTOs);

        ReportDetailDTO result = reportMapper.toReportDetailDTO(testReport);

        assertNotNull(result);
        assertEquals(reportId, result.getReportID());
        assertEquals("Test Report", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("EVENT_ISSUE", result.getCategory());
        assertEquals("event-123", result.getCategoryReference());
        assertEquals("/path/to/file.jpg", result.getAttachmentPath());
        assertEquals(ReportStatus.PENDING.getValue(), result.getStatus());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals("user-123", result.getCreatedBy());
        assertEquals(messageDTOs, result.getMessages());
        verify(reportMessageMapper).toReportMessageDTOs(messages);
    }

    @Test
    void toReportDetailDTO_ShouldReturnNull_WhenReportIsNull() {
        ReportDetailDTO result = reportMapper.toReportDetailDTO(null);

        assertNull(result);
        verify(reportMessageMapper, never()).toReportMessageDTOs(anyList());
    }

    @Test
    void toReportListDTOs_ShouldMapAllReports() {
        Report secondReport = new Report();
        try {
            java.lang.reflect.Field idField = Report.class.getDeclaredField("reportID");
            idField.setAccessible(true);
            idField.set(secondReport, UUID.randomUUID());

            java.lang.reflect.Field titleField = Report.class.getDeclaredField("title");
            titleField.setAccessible(true);
            titleField.set(secondReport, "Second Report");
        } catch (Exception e) {
            fail("Failed to set up test data: " + e.getMessage());
        }

        List<Report> reports = Arrays.asList(testReport, secondReport);

        List<ReportListDTO> result = reportMapper.toReportListDTOs(reports);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(reportId, result.get(0).getReportID());
        assertEquals("Test Report", result.get(0).getTitle());
        assertEquals("Second Report", result.get(1).getTitle());
    }

    @Test
    void toReportListDTOs_ShouldReturnEmptyList_WhenReportsIsNull() {
        List<ReportListDTO> result = reportMapper.toReportListDTOs(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}