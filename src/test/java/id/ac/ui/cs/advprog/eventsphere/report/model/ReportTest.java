package id.ac.ui.cs.advprog.eventsphere.report.model;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    private Report report;
    private final String TITLE = "Test Report";
    private final String DESCRIPTION = "Test Description";
    private final String CATEGORY = ReportCategory.PAYMENT.getValue();
    private final String CATEGORY_REFERENCE = "PAYMENT-123";
    private final String CREATED_BY = "testuser";

    @BeforeEach
    void setUp() {
        report = new Report(TITLE, DESCRIPTION, CATEGORY, CATEGORY_REFERENCE, CREATED_BY);
    }

    @Test
    void testReportCreation() {
        assertEquals(TITLE, report.getTitle());
        assertEquals(DESCRIPTION, report.getDescription());
        assertEquals(CATEGORY, report.getCategory());
        assertEquals(CATEGORY_REFERENCE, report.getCategoryReference());
        assertEquals(CREATED_BY, report.getCreatedBy());
        assertTrue(report.getMessages().isEmpty());

        // Default status
        assertEquals(ReportStatus.PENDING.getValue(), report.getStatus());

        // CreatedAt harusnya diset ke waktu sekarang (kurang lebih)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = report.getCreatedAt();

        // Buat sedikit perbedaan waktu (5 detik) karena waktu tidak akan sama persis
        long timeDiffSeconds = Math.abs(createdAt.toEpochSecond(java.time.ZoneOffset.UTC) -
                now.toEpochSecond(java.time.ZoneOffset.UTC));
        assertTrue(timeDiffSeconds < 5, "Waktu pembuatan harusnya dekat dengan waktu saat ini");
    }

    @Test
    void testUpdateStatus() {
        // Default status
        assertEquals(ReportStatus.PENDING.getValue(), report.getStatus());

        report.updateStatus(ReportStatus.ON_PROGRESS.getValue());
        assertEquals(ReportStatus.ON_PROGRESS.getValue(), report.getStatus());

        report.updateStatus(ReportStatus.RESOLVED.getValue());
        assertEquals(ReportStatus.RESOLVED.getValue(), report.getStatus());
    }

    @Test
    void testAddMessage() {
        assertTrue(report.getMessages().isEmpty());

        // Penambahan pesan ke-1
        String messageText = "Ini adalah pesan test";
        String sender = "pengirim1";
        report.addMessage(messageText, sender);

        List<ReportMessage> messages = report.getMessages();
        assertEquals(1, messages.size());

        ReportMessage addedMessage = messages.get(0);
        assertEquals(messageText, addedMessage.getMessage());
        assertEquals(sender, addedMessage.getSender());
        assertEquals(report, addedMessage.getReport());

        // Timestamp harusnya diset ke waktu sekarang (kurang lebih)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime messageTime = addedMessage.getTimestamp();

        // Ada sedikit perbedaan waktu (5 detik)
        long timeDiffSeconds = Math.abs(messageTime.toEpochSecond(java.time.ZoneOffset.UTC) -
                now.toEpochSecond(java.time.ZoneOffset.UTC));
        assertTrue(timeDiffSeconds < 5, "Waktu pesan harusnya dekat dengan waktu saat ini");

        // Penambahan pesan ke-2
        String messageText2 = "Pesan test kedua";
        String sender2 = "pengirim2";
        report.addMessage(messageText2, sender2);

        assertEquals(2, report.getMessages().size());
        assertEquals(messageText2, report.getMessages().get(1).getMessage());
        assertEquals(sender2, report.getMessages().get(1).getSender());
    }

    @Test
    void testToString() {
        String reportString = report.toString();

        // ToString seharusnya berisi reportID dan field penting, tapi tidak messages
        assertTrue(reportString.contains("reportID"));
        assertTrue(reportString.contains(TITLE));
        assertTrue(reportString.contains(CATEGORY));
        assertTrue(reportString.contains(CATEGORY_REFERENCE));
        assertTrue(reportString.contains(ReportStatus.PENDING.getValue()));
        assertTrue(reportString.contains(CREATED_BY));

        // Seharusnya tidak berisi list messages (dikecualikan dalam @ToString)
        assertFalse(reportString.contains("messages="));
    }

    @Test
    void testNoArgsConstructor() {
        Report emptyReport = new Report();

        assertNull(emptyReport.getReportID());
        assertNull(emptyReport.getTitle());
        assertNull(emptyReport.getDescription());
        assertNull(emptyReport.getCategory());
        assertNull(emptyReport.getCategoryReference());
        assertNull(emptyReport.getStatus());
        assertNull(emptyReport.getCreatedAt());
        assertNull(emptyReport.getCreatedBy());
        assertNotNull(emptyReport.getMessages());  // Seharusnya diinisialisasi ke list kosong
        assertTrue(emptyReport.getMessages().isEmpty());
    }
}
