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
        report = new Report("Test Report", ReportCategory.PAYMENT.getValue(), "PAYMENT-123", "creator");
        reportMessage = new ReportMessage(report, MESSAGE_TEXT, SENDER);
    }

    @Test
    void testReportMessageCreation() {
        // Verifikasi semua field sudah diset dengan benar
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
        // Report null harusnya menyebabkan IllegalArgumentException
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(null, MESSAGE_TEXT, SENDER);
        });

        // Pesan error harusnya sesuai
        assertEquals("Report cannot be null", exception.getMessage());
    }

    @Test
    void testConstructorValidation_EmptyMessage() {
        // Pesan kosong harusnya menyebabkan IllegalArgumentException
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, "", SENDER);
        });
        assertEquals("Message cannot be empty", exception1.getMessage());

        // Pesan null harusnya menyebabkan IllegalArgumentException
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, null, SENDER);
        });
        assertEquals("Message cannot be empty", exception2.getMessage());

        // Pesan hanya whitespace harusnya menyebabkan IllegalArgumentException
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, "    ", SENDER);
        });
        assertEquals("Message cannot be empty", exception3.getMessage());
    }

    @Test
    void testConstructorValidation_EmptySender() {
        // Sender kosong harusnya menyebabkan IllegalArgumentException
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, MESSAGE_TEXT, "");
        });
        assertEquals("Sender cannot be empty", exception1.getMessage());

        // Sender null harusnya menyebabkan IllegalArgumentException
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, MESSAGE_TEXT, null);
        });
        assertEquals("Sender cannot be empty", exception2.getMessage());

        // Sender hanya whitespace harusnya menyebabkan IllegalArgumentException
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> {
            new ReportMessage(report, MESSAGE_TEXT, "   ");
        });
        assertEquals("Sender cannot be empty", exception3.getMessage());
    }

//    @Test
//    void testEquals() {
//        // Buat report message dengan data yang sama tapi instance Report yang berbeda
//        Report otherReport = new Report("Test Report", ReportCategory.PAYMENT.getValue(), "PAYMENT-123", "creator");
//        ReportMessage sameMessage = new ReportMessage(otherReport, MESSAGE_TEXT, SENDER);
//
//        // Seharusnya tidak sama karena UUID berbeda
//        assertNotEquals(reportMessage, sameMessage);
//
//        // Buat report message dengan constructor all-args untuk set ID yang sama
//        UUID id = UUID.randomUUID();
//        Report report1 = new Report("Test", ReportCategory.PAYMENT.getValue(), "REF-1", "creator1");
//        Report report2 = new Report("Different", ReportCategory.TICKET.getValue(), "REF-2", "creator2");
//
//        ReportMessage message1 = new ReportMessage(id, report1, "Pesan 1",
//                LocalDateTime.now(), "sender1");
//        ReportMessage message2 = new ReportMessage(id, report2, "Pesan 2",
//                LocalDateTime.now().minusDays(1), "sender2");
//
//        // Seharusnya sama karena ID sama meskipun report berbeda
//        // ReportMessage mengecualikan field report dalam equals
//        assertEquals(message1, message2);
//    }

    @Test
    void testToString() {
        String messageString = reportMessage.toString();

        // toString seharusnya berisi messageID dan field penting, tapi tidak report
        assertTrue(messageString.contains("messageID"));
        assertTrue(messageString.contains(MESSAGE_TEXT));
        assertTrue(messageString.contains(SENDER));

        // Seharusnya tidak berisi report (dikecualikan dalam @ToString)
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
        // Buat ReportMessage dengan no-args constructor
        ReportMessage emptyMessage = new ReportMessage();

        // Verifikasi semua field null
        assertNull(emptyMessage.getMessageID());
        assertNull(emptyMessage.getReport());
        assertNull(emptyMessage.getMessage());
        assertNull(emptyMessage.getTimestamp());
        assertNull(emptyMessage.getSender());
    }
}