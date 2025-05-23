package id.ac.ui.cs.advprog.eventsphere.report.mapper;

import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportMessageMapperTest {

    private ReportMessageMapper reportMessageMapper;
    private ReportMessage testMessage;
    private UUID messageId;
    private UUID reportId;
    private LocalDateTime timestamp;

    @BeforeEach
    void setUp() {
        reportMessageMapper = new ReportMessageMapper();

        messageId = UUID.randomUUID();
        reportId = UUID.randomUUID();
        timestamp = LocalDateTime.now();

        Report report = new Report();
        testMessage = new ReportMessage();

        // Set message fields dengan reflection karena tidak ada setter
        try {
            java.lang.reflect.Field idField = ReportMessage.class.getDeclaredField("messageID");
            idField.setAccessible(true);
            idField.set(testMessage, messageId);

            java.lang.reflect.Field reportField = ReportMessage.class.getDeclaredField("report");
            reportField.setAccessible(true);
            reportField.set(testMessage, report);

            java.lang.reflect.Field messageField = ReportMessage.class.getDeclaredField("message");
            messageField.setAccessible(true);
            messageField.set(testMessage, "Test message content");

            java.lang.reflect.Field timestampField = ReportMessage.class.getDeclaredField("timestamp");
            timestampField.setAccessible(true);
            timestampField.set(testMessage, timestamp);

            java.lang.reflect.Field senderField = ReportMessage.class.getDeclaredField("sender");
            senderField.setAccessible(true);
            senderField.set(testMessage, "test-user");
        } catch (Exception e) {
            fail("Failed to set up test data: " + e.getMessage());
        }
    }

    @Test
    void toReportMessageDTO_ShouldMapAllFields() {
        ReportMessageDTO result = reportMessageMapper.toReportMessageDTO(testMessage);

        assertNotNull(result);
        assertEquals(messageId, result.getMessageID());
        assertEquals("Test message content", result.getMessage());
        assertEquals(timestamp, result.getTimestamp());
        assertEquals("test-user", result.getSender());
    }

    @Test
    void toReportMessageDTO_ShouldReturnNull_WhenMessageIsNull() {
        ReportMessageDTO result = reportMessageMapper.toReportMessageDTO(null);

        assertNull(result);
    }

    @Test
    void toReportMessageDTOs_ShouldMapAllMessages() {
        ReportMessage secondMessage = new ReportMessage();
        try {
            java.lang.reflect.Field idField = ReportMessage.class.getDeclaredField("messageID");
            idField.setAccessible(true);
            idField.set(secondMessage, UUID.randomUUID());

            java.lang.reflect.Field messageField = ReportMessage.class.getDeclaredField("message");
            messageField.setAccessible(true);
            messageField.set(secondMessage, "Second message");

            java.lang.reflect.Field timestampField = ReportMessage.class.getDeclaredField("timestamp");
            timestampField.setAccessible(true);
            timestampField.set(secondMessage, LocalDateTime.now());

            java.lang.reflect.Field senderField = ReportMessage.class.getDeclaredField("sender");
            senderField.setAccessible(true);
            senderField.set(secondMessage, "user2");
        } catch (Exception e) {
            fail("Failed to set up test data: " + e.getMessage());
        }

        List<ReportMessage> messages = Arrays.asList(testMessage, secondMessage);

        List<ReportMessageDTO> result = reportMessageMapper.toReportMessageDTOs(messages);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(messageId, result.get(0).getMessageID());
        assertEquals("Test message content", result.get(0).getMessage());
        assertEquals("user2", result.get(1).getSender());
    }

    @Test
    void toReportMessageDTOs_ShouldReturnEmptyList_WhenMessagesIsNull() {
        List<ReportMessageDTO> result = reportMessageMapper.toReportMessageDTOs(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toReportMessageDTOs_ShouldReturnEmptyList_WhenMessagesIsEmpty() {
        List<ReportMessageDTO> result = reportMessageMapper.toReportMessageDTOs(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}