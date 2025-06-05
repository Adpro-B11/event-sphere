package id.ac.ui.cs.advprog.eventsphere.report.model;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportMessageTest {

    private Report report;
    private ReportMessage reportMessage;
    private final String MESSAGE_TEXT = "Ini adalah pesan test";
    private final String SENDER = "testuser";

    @BeforeEach
    void setUp() {
        report = new Report(
                "Test Report",
                "Test Deskripsi",
                ReportCategory.PAYMENT.getValue(),
                "PAYMENT-123",
                "creator");
        reportMessage = new ReportMessage(report, MESSAGE_TEXT, SENDER);
    }

    @Test
    void testReportMessageCreation() {
        assertEquals(MESSAGE_TEXT, reportMessage.getMessage());
        assertEquals(SENDER, reportMessage.getSender());
        assertEquals(report, reportMessage.getReport());

        // Timestamp harusnya diset ke waktu sekarang (kurang lebih)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timestamp = reportMessage.getTimestamp();

        // Ada sedikit perbedaan waktu (5 detik)
        long timeDiffSeconds = Math.abs(timestamp.toEpochSecond(java.time.ZoneOffset.UTC) -
                now.toEpochSecond(java.time.ZoneOffset.UTC));
        assertTrue(timeDiffSeconds < 5, "Timestamp harusnya dekat dengan waktu saat ini");
    }

    @Test
    void testConstructorValidation_NullReport() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(null, MESSAGE_TEXT, SENDER);
        });

        assertEquals("Report cannot be null", exception.getMessage());
    }

    @Test
    void testConstructorValidation_EmptyMessage() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, "", SENDER);
        });
        assertEquals("Message cannot be empty", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, null, SENDER);
        });
        assertEquals("Message cannot be empty", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, "    ", SENDER);
        });
        assertEquals("Message cannot be empty", exception3.getMessage());
    }

    @Test
    void testConstructorValidation_EmptySender() {
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, MESSAGE_TEXT, "");
        });
        assertEquals("Sender cannot be empty", exception1.getMessage());

        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, MESSAGE_TEXT, null);
        });
        assertEquals("Sender cannot be empty", exception2.getMessage());

        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, MESSAGE_TEXT, "   ");
        });
        assertEquals("Sender cannot be empty", exception3.getMessage());
    }

    @Test
    void testToString() {
        String messageString = reportMessage.toString();

        assertTrue(messageString.contains("messageID"));
        assertTrue(messageString.contains(MESSAGE_TEXT));
        assertTrue(messageString.contains(SENDER));

        assertFalse(messageString.contains("report="));
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.now().minusDays(1);

        ReportMessage customMessage = new ReportMessage(id, report, MESSAGE_TEXT, time, SENDER);

        // Verifikasi field-field sudah diset dengan benar
        assertEquals(id, customMessage.getMessageID());
        assertEquals(report, customMessage.getReport());
        assertEquals(MESSAGE_TEXT, customMessage.getMessage());
        assertEquals(time, customMessage.getTimestamp());
        assertEquals(SENDER, customMessage.getSender());
    }

    @Test
    void testNoArgsConstructor() {
        ReportMessage emptyMessage = new ReportMessage();

        assertNull(emptyMessage.getMessageID());
        assertNull(emptyMessage.getReport());
        assertNull(emptyMessage.getMessage());
        assertNull(emptyMessage.getTimestamp());
        assertNull(emptyMessage.getSender());
    }
}
