package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import id.ac.ui.cs.advprog.eventsphere.report.repository.ReportMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportMessageServiceTest {

    @Mock
    private ReportMessageRepository reportMessageRepository;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private ReportMessageServiceImpl reportMessageService;

    private ReportMessage reportMessage;
    private Report report;
    private UUID reportId;
    private UUID messageId;
    private String messageText;
    private String sender;

    @BeforeEach
    void setUp() {
        reportId = UUID.randomUUID();
        messageId = UUID.randomUUID();
        messageText = "Mohon info lebih lanjut tentang masalah tiket saya";
        sender = "user123";

        report = new Report(
                "Tiket tidak valid",
                "Tiket yang saya beli tidak bisa di-scan oleh panitia",
                "TICKET",
                "TICKET-123",
                sender
        );

        // Set report ID melalui reflection (karena reportID di generate oleh JPA)
        try {
            java.lang.reflect.Field reportIDField = Report.class.getDeclaredField("reportID");
            reportIDField.setAccessible(true);
            reportIDField.set(report, reportId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        reportMessage = new ReportMessage(report, messageText, sender);

        // Set message ID dan timestamp melalui reflection
        try {
            java.lang.reflect.Field messageIDField = ReportMessage.class.getDeclaredField("messageID");
            messageIDField.setAccessible(true);
            messageIDField.set(reportMessage, messageId);

            java.lang.reflect.Field timestampField = ReportMessage.class.getDeclaredField("timestamp");
            timestampField.setAccessible(true);
            timestampField.set(reportMessage, LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateMessage() {
        when(reportMessageRepository.save(any(ReportMessage.class))).thenReturn(reportMessage);

        ReportMessage savedMessage = reportMessageService.createMessage(reportMessage);

        assertNotNull(savedMessage);
        assertEquals(messageId, savedMessage.getMessageID());
        assertEquals(messageText, savedMessage.getMessage());
        assertEquals(sender, savedMessage.getSender());
        verify(reportMessageRepository).save(reportMessage);
    }

    @Test
    void testCreateMessageFromDTO() {
        when(reportService.findReportById(reportId)).thenReturn(report);
        when(reportMessageRepository.save(any(ReportMessage.class))).thenAnswer(invocation -> {
            ReportMessage savedMessage = invocation.getArgument(0);
            // Set messageID pada objek yang baru dibuat melalui reflection
            try {
                java.lang.reflect.Field messageIDField = ReportMessage.class.getDeclaredField("messageID");
                messageIDField.setAccessible(true);
                messageIDField.set(savedMessage, messageId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return savedMessage;
        });

        ReportMessage result = reportMessageService.createMessageFromDTO(reportId, messageText, sender);

        assertNotNull(result);
        assertEquals(messageText, result.getMessage());
        assertEquals(sender, result.getSender());
        assertEquals(report, result.getReport());
        verify(reportService).findReportById(reportId);
        verify(reportMessageRepository).save(any(ReportMessage.class));
    }

    @Test
    void testCreateMessageFromDTO_ReportNotFound() {
        when(reportService.findReportById(reportId)).thenThrow(new IllegalArgumentException("Report not found"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportMessageService.createMessageFromDTO(reportId, messageText, sender);
        });

        assertEquals("Report not found", exception.getMessage());
        verify(reportService).findReportById(reportId);
        verify(reportMessageRepository, never()).save(any(ReportMessage.class));
    }

    @Test
    void testFindMessagesByReportId() {
        List<ReportMessage> messages = Arrays.asList(reportMessage);
        when(reportMessageRepository.findByReportReportID(reportId)).thenReturn(messages);

        List<ReportMessage> result = reportMessageService.findMessagesByReportId(reportId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(messageId, result.get(0).getMessageID());
        assertEquals(messageText, result.get(0).getMessage());
        verify(reportMessageRepository).findByReportReportID(reportId);
    }

    @Test
    void testFindMessageById() {
        when(reportMessageRepository.findById(messageId)).thenReturn(Optional.of(reportMessage));

        ReportMessage result = reportMessageService.findMessageById(messageId);

        assertNotNull(result);
        assertEquals(messageId, result.getMessageID());
        assertEquals(messageText, result.getMessage());
        assertEquals(sender, result.getSender());
        verify(reportMessageRepository).findById(messageId);
    }

    @Test
    void testFindMessageById_MessageNotFound() {
        when(reportMessageRepository.findById(messageId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportMessageService.findMessageById(messageId);
        });

        assertEquals("Message not found", exception.getMessage());
        verify(reportMessageRepository).findById(messageId);
    }

    @Test
    void testCreateMessage_WithInvalidMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, "", sender);
        });

        assertEquals("Message cannot be empty", exception.getMessage());
        verify(reportMessageRepository, never()).save(any(ReportMessage.class));
    }

    @Test
    void testCreateMessage_WithInvalidSender() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, messageText, "");
        });

        assertEquals("Sender cannot be empty", exception.getMessage());
        verify(reportMessageRepository, never()).save(any(ReportMessage.class));
    }

    @Test
    void testCreateMessage_WithNullReport() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(null, messageText, sender);
        });

        assertEquals("Report cannot be null", exception.getMessage());
        verify(reportMessageRepository, never()).save(any(ReportMessage.class));
    }
}
