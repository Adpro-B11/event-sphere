package id.ac.ui.cs.advprog.eventsphere.report.model;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportTest {

    private Report report;
    private final String TITLE = "Test Report";
    private final String CATEGORY = ReportCategory.PAYMENT.getValue();
    private final String CATEGORY_REFERENCE = "PAYMENT-123";
    private final String CREATED_BY = "testuser";

    @BeforeEach
    void setUp() {
        report = new Report(TITLE, CATEGORY, CATEGORY_REFERENCE, CREATED_BY);
    }

    @Test
    void testReportCreation() {
        // Verifikasi semua field sudah diset dengan benar
        assertEquals(TITLE, report.getTitle());
        assertEquals(CATEGORY, report.getCategory());
        assertEquals(CATEGORY_REFERENCE, report.getCategoryReference());
        assertEquals(CREATED_BY, report.getCreatedBy());

        // Status harusnya PENDING secara default
        assertEquals(ReportStatus.PENDING.getValue(), report.getStatus());

        // CreatedAt harusnya diset ke waktu sekarang (kurang lebih)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdAt = report.getCreatedAt();

        // Buat sedikit perbedaan waktu (5 detik) karena waktu tidak akan sama persis
        long timeDiffSeconds = Math.abs(createdAt.toEpochSecond(java.time.ZoneOffset.UTC) -
                now.toEpochSecond(java.time.ZoneOffset.UTC));
        assertTrue(timeDiffSeconds < 5, "Waktu pembuatan harusnya dekat dengan waktu saat ini");

        // List messages harusnya kosong
        assertTrue(report.getMessages().isEmpty());
    }

    @Test
    void testUpdateStatus() {
        // Status awal harusnya PENDING
        assertEquals(ReportStatus.PENDING.getValue(), report.getStatus());

        // Update ke ON_PROGRESS
        report.updateStatus(ReportStatus.ON_PROGRESS.getValue());
        assertEquals(ReportStatus.ON_PROGRESS.getValue(), report.getStatus());

        // Update ke RESOLVED
        report.updateStatus(ReportStatus.RESOLVED.getValue());
        assertEquals(ReportStatus.RESOLVED.getValue(), report.getStatus());
    }

    @Test
    void testAddMessage() {
        // Awalnya, tidak ada pesan
        assertTrue(report.getMessages().isEmpty());

        // Tambahkan pesan
        String messageText = "Ini adalah pesan test";
        String sender = "pengirim1";
        report.addMessage(messageText, sender);

        // Verifikasi pesan telah ditambahkan
        List<ReportMessage> messages = report.getMessages();
        assertEquals(1, messages.size());

        ReportMessage addedMessage = messages.get(0);
        assertEquals(messageText, addedMessage.getMessage());
        assertEquals(sender, addedMessage.getSender());
        assertEquals(report, addedMessage.getReport());

        // Timestamp harusnya diset ke waktu sekarang (kurang lebih)
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime messageTime = addedMessage.getTimestamp();

        // Izinkan sedikit perbedaan waktu (5 detik)
        long timeDiffSeconds = Math.abs(messageTime.toEpochSecond(java.time.ZoneOffset.UTC) -
                now.toEpochSecond(java.time.ZoneOffset.UTC));
        assertTrue(timeDiffSeconds < 5, "Waktu pesan harusnya dekat dengan waktu saat ini");

        // Tambahkan pesan lain
        String messageText2 = "Pesan test kedua";
        String sender2 = "pengirim2";
        report.addMessage(messageText2, sender2);

        // Verifikasi kedua pesan ada
        assertEquals(2, report.getMessages().size());
        assertEquals(messageText2, report.getMessages().get(1).getMessage());
        assertEquals(sender2, report.getMessages().get(1).getSender());
    }

    @Test
    void testReportEquals() {
        // Buat report baru dengan field yang sama
        Report sameReport = new Report(TITLE, CATEGORY, CATEGORY_REFERENCE, CREATED_BY);

        // Keduanya seharusnya tidak sama karena UUID berbeda
        assertNotEquals(report, sameReport);

        // Buat report dengan constructor all-args untuk set ID yang sama
        UUID id = UUID.randomUUID();
        Report report1 = new Report(id, TITLE, CATEGORY, CATEGORY_REFERENCE, ReportStatus.PENDING.getValue(),
                LocalDateTime.now(), CREATED_BY, List.of());
        Report report2 = new Report(id, "Judul Berbeda", ReportCategory.TICKET.getValue(), "TICKET-456",
                ReportStatus.RESOLVED.getValue(), LocalDateTime.now().minusDays(1),
                "userLain", List.of());

        // Keduanya seharusnya sama karena ID sama, meskipun field lain berbeda
        assertEquals(report1, report2);
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
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        List<ReportMessage> messages = List.of(
                new ReportMessage(null, "Pesan test", "tester")
        );

        Report customReport = new Report(id, TITLE, CATEGORY, CATEGORY_REFERENCE,
                ReportStatus.RESOLVED.getValue(), created, CREATED_BY, messages);

        assertEquals(id, customReport.getReportID());
        assertEquals(TITLE, customReport.getTitle());
        assertEquals(CATEGORY, customReport.getCategory());
        assertEquals(CATEGORY_REFERENCE, customReport.getCategoryReference());
        assertEquals(ReportStatus.RESOLVED.getValue(), customReport.getStatus());
        assertEquals(created, customReport.getCreatedAt());
        assertEquals(CREATED_BY, customReport.getCreatedBy());
        assertEquals(1, customReport.getMessages().size());
    }

    @Test
    void testNoArgsConstructor() {
        Report emptyReport = new Report();

        assertNull(emptyReport.getReportID());
        assertNull(emptyReport.getTitle());
        assertNull(emptyReport.getCategory());
        assertNull(emptyReport.getCategoryReference());
        assertNull(emptyReport.getStatus());
        assertNull(emptyReport.getCreatedAt());
        assertNull(emptyReport.getCreatedBy());
        assertNotNull(emptyReport.getMessages());  // Seharusnya diinisialisasi ke list kosong
        assertTrue(emptyReport.getMessages().isEmpty());
    }
}