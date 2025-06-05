package id.ac.ui.cs.advprog.eventsphere.report.repository;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportMessageRepositoryTest {

    @Mock
    private ReportMessageRepository reportMessageRepository;

    private Report report1;
    private Report report2;
    private ReportMessage message1;
    private ReportMessage message2;
    private ReportMessage message3;
    private List<ReportMessage> allMessages;
    private List<ReportMessage> report1Messages;
    private List<ReportMessage> report2Messages;
    private UUID reportId1, reportId2, messageId1, messageId2, messageId3;

    @BeforeEach
    void setUp() {
        reportId1 = UUID.randomUUID();
        reportId2 = UUID.randomUUID();
        messageId1 = UUID.randomUUID();
        messageId2 = UUID.randomUUID();
        messageId3 = UUID.randomUUID();

        report1 = new Report(
                "Pembayaran Gagal",
                "Deskripsi: Pembayaran tidak berhasil dilakukan",
                ReportCategory.PAYMENT.getValue(),
                "PAY-123",
                "user1"
        );

        report2 = new Report(
                "Tiket Tidak Muncul",
                "Deskripsi: Tiket tidak muncul di aplikasi",
                ReportCategory.TICKET.getValue(),
                "TIX-456",
                "user2"
        );

        // Set report ID melalui reflection
        try {
            java.lang.reflect.Field reportIDField = Report.class.getDeclaredField("reportID");
            reportIDField.setAccessible(true);
            reportIDField.set(report1, reportId1);
            reportIDField.set(report2, reportId2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        message1 = new ReportMessage(report1, "Pesan pertama untuk report 1", "user1");
        message2 = new ReportMessage(report1, "Pesan kedua untuk report 1", "admin");
        message3 = new ReportMessage(report2, "Pesan pertama untuk report 2", "user2");

        // Set message ID dan timestamp melalui reflection
        try {
            java.lang.reflect.Field messageIDField = ReportMessage.class.getDeclaredField("messageID");
            messageIDField.setAccessible(true);
            messageIDField.set(message1, messageId1);
            messageIDField.set(message2, messageId2);
            messageIDField.set(message3, messageId3);

            java.lang.reflect.Field timestampField = ReportMessage.class.getDeclaredField("timestamp");
            timestampField.setAccessible(true);
            LocalDateTime now = LocalDateTime.now();
            timestampField.set(message1, now);
            timestampField.set(message2, now.plusMinutes(5));
            timestampField.set(message3, now.plusMinutes(10));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // List untuk pengujian
        allMessages = new ArrayList<>();
        allMessages.add(message1);
        allMessages.add(message2);
        allMessages.add(message3);

        report1Messages = new ArrayList<>();
        report1Messages.add(message1);
        report1Messages.add(message2);

        report2Messages = new ArrayList<>();
        report2Messages.add(message3);
    }

    @Test
    void testFindById() {
        when(reportMessageRepository.findById(any(UUID.class))).thenReturn(Optional.of(message1));

        Optional<ReportMessage> result = reportMessageRepository.findById(messageId1);

        assertTrue(result.isPresent());
        assertEquals("Pesan pertama untuk report 1", result.get().getMessage());
        verify(reportMessageRepository).findById(messageId1);
    }

    @Test
    void testFindAll() {
        when(reportMessageRepository.findAll()).thenReturn(allMessages);

        List<ReportMessage> results = reportMessageRepository.findAll();

        assertEquals(3, results.size());
        verify(reportMessageRepository).findAll();
    }

    @Test
    void testSave() {
        when(reportMessageRepository.save(any(ReportMessage.class))).thenReturn(message1);

        ReportMessage result = reportMessageRepository.save(message1);

        assertEquals("Pesan pertama untuk report 1", result.getMessage());
        verify(reportMessageRepository).save(message1);
    }

    @Test
    void testFindByReportReportID() {
        when(reportMessageRepository.findByReportReportID(reportId1)).thenReturn(report1Messages);

        List<ReportMessage> results = reportMessageRepository.findByReportReportID(reportId1);

        assertEquals(2, results.size());
        assertEquals("Pesan pertama untuk report 1", results.get(0).getMessage());
        assertEquals("Pesan kedua untuk report 1", results.get(1).getMessage());
        verify(reportMessageRepository).findByReportReportID(reportId1);
    }

    @Test
    void testFindByReportReportID_EmptyList() {
        UUID nonExistentReportId = UUID.randomUUID();
        when(reportMessageRepository.findByReportReportID(nonExistentReportId)).thenReturn(new ArrayList<>());

        List<ReportMessage> results = reportMessageRepository.findByReportReportID(nonExistentReportId);

        assertTrue(results.isEmpty());
        verify(reportMessageRepository).findByReportReportID(nonExistentReportId);
    }

    @Test
    void testDelete() {
        doNothing().when(reportMessageRepository).delete(any(ReportMessage.class));

        reportMessageRepository.delete(message1);

        verify(reportMessageRepository).delete(message1);
    }
}
