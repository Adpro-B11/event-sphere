package id.ac.ui.cs.advprog.eventsphere.report.repository;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
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
public class ReportRepositoryTest {

    @Mock
    private ReportRepository reportRepository;

    private Report report1;
    private Report report2;
    private List<Report> allReports;
    private UUID uuid1, uuid2;

    @BeforeEach
    void setUp() {
        // Bikin UUID buat testing
        uuid1 = UUID.randomUUID();
        uuid2 = UUID.randomUUID();

        // Bikin report contoh untuk testing
        report1 = new Report(
                "Pembayaran Gagal",
                ReportCategory.PAYMENT.getValue(),
                "PAY-123",
                "user1@example.com"
        );

        report2 = new Report(
                "Tiket Tidak Muncul",
                ReportCategory.TICKET.getValue(),
                "TIX-456",
                "user2@example.com"
        );

        // Bikin list semua report
        allReports = new ArrayList<>();
        allReports.add(report1);
        allReports.add(report2);
    }

    @Test
    void testFindById() {
        when(reportRepository.findById(any(UUID.class))).thenReturn(Optional.of(report1));

        Optional<Report> result = reportRepository.findById(uuid1);

        assertTrue(result.isPresent());
        assertEquals("Pembayaran Gagal", result.get().getTitle());
        verify(reportRepository).findById(uuid1);
    }

    @Test
    void testFindAll() {
        when(reportRepository.findAll()).thenReturn(allReports);

        List<Report> results = reportRepository.findAll();

        assertEquals(2, results.size());
        verify(reportRepository).findAll();
    }

    @Test
    void testSave() {
        when(reportRepository.save(any(Report.class))).thenReturn(report1);

        Report result = reportRepository.save(report1);

        assertEquals("Pembayaran Gagal", result.getTitle());
        verify(reportRepository).save(report1);
    }

    @Test
    void testFindByStatus() {
        List<Report> pendingReports = new ArrayList<>();
        pendingReports.add(report1);

        when(reportRepository.findByStatus(anyString())).thenReturn(pendingReports);

        List<Report> results = reportRepository.findByStatus(ReportStatus.PENDING.getValue());

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByStatus(ReportStatus.PENDING.getValue());
    }

    @Test
    void testFindByCategory() {
        List<Report> paymentReports = new ArrayList<>();
        paymentReports.add(report1);

        when(reportRepository.findByCategory(ReportCategory.PAYMENT.getValue()))
                .thenReturn(paymentReports);

        List<Report> results = reportRepository.findByCategory(ReportCategory.PAYMENT.getValue());

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByCategory(ReportCategory.PAYMENT.getValue());
    }

    @Test
    void testFindByCreatedBy() {
        List<Report> userReports = new ArrayList<>();
        userReports.add(report1);

        when(reportRepository.findByCreatedBy("user1@example.com")).thenReturn(userReports);

        List<Report> results = reportRepository.findByCreatedBy("user1@example.com");

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByCreatedBy("user1@example.com");
    }

    @Test
    void testFindByCreatedAtBetween() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        when(reportRepository.findByCreatedAtBetween(start, end)).thenReturn(allReports);

        List<Report> results = reportRepository.findByCreatedAtBetween(start, end);

        assertEquals(2, results.size());
        verify(reportRepository).findByCreatedAtBetween(start, end);
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        List<Report> filteredReports = new ArrayList<>();
        filteredReports.add(report1);

        when(reportRepository.findByTitleContainingIgnoreCase("Pembayaran")).thenReturn(filteredReports);

        List<Report> results = reportRepository.findByTitleContainingIgnoreCase("Pembayaran");

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByTitleContainingIgnoreCase("Pembayaran");
    }

    @Test
    void testFindByCategoryReference() {
        List<Report> payReports = new ArrayList<>();
        payReports.add(report1);

        when(reportRepository.findByCategoryReference("PAY-123")).thenReturn(payReports);

        List<Report> results = reportRepository.findByCategoryReference("PAY-123");

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByCategoryReference("PAY-123");
    }

    @Test
    void testFindByCategoryAndStatus() {
        List<Report> pendingPaymentReports = new ArrayList<>();
        pendingPaymentReports.add(report1);

        when(reportRepository.findByCategoryAndStatus(
                ReportCategory.PAYMENT.getValue(),
                ReportStatus.PENDING.getValue()
        )).thenReturn(pendingPaymentReports);

        List<Report> results = reportRepository.findByCategoryAndStatus(
                ReportCategory.PAYMENT.getValue(),
                ReportStatus.PENDING.getValue()
        );

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByCategoryAndStatus(
                ReportCategory.PAYMENT.getValue(),
                ReportStatus.PENDING.getValue()
        );
    }

    @Test
    void testFindByCreatedByAndStatus() {
        List<Report> userPendingReports = new ArrayList<>();
        userPendingReports.add(report1);

        when(reportRepository.findByCreatedByAndStatus(
                "user1@example.com",
                ReportStatus.PENDING.getValue()
        )).thenReturn(userPendingReports);

        List<Report> results = reportRepository.findByCreatedByAndStatus(
                "user1@example.com",
                ReportStatus.PENDING.getValue()
        );

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByCreatedByAndStatus(
                "user1@example.com",
                ReportStatus.PENDING.getValue()
        );
    }

    @Test
    void testFindAllByOrderByCreatedAtDesc() {
        when(reportRepository.findAllByOrderByCreatedAtDesc()).thenReturn(allReports);

        List<Report> results = reportRepository.findAllByOrderByCreatedAtDesc();

        assertEquals(2, results.size());
        verify(reportRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void testFindByCategoryOrderByCreatedAtDesc() {
        List<Report> paymentReports = new ArrayList<>();
        paymentReports.add(report1);

        when(reportRepository.findByCategoryOrderByCreatedAtDesc(
                ReportCategory.PAYMENT.getValue()
        )).thenReturn(paymentReports);

        List<Report> results = reportRepository.findByCategoryOrderByCreatedAtDesc(
                ReportCategory.PAYMENT.getValue()
        );

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByCategoryOrderByCreatedAtDesc(
                ReportCategory.PAYMENT.getValue()
        );
    }

    @Test
    void testFindByStatusOrderByCreatedAtDesc() {
        List<Report> pendingReports = new ArrayList<>();
        pendingReports.add(report1);

        when(reportRepository.findByStatusOrderByCreatedAtDesc(
                ReportStatus.PENDING.getValue()
        )).thenReturn(pendingReports);

        List<Report> results = reportRepository.findByStatusOrderByCreatedAtDesc(
                ReportStatus.PENDING.getValue()
        );

        assertEquals(1, results.size());
        assertEquals("Pembayaran Gagal", results.get(0).getTitle());
        verify(reportRepository).findByStatusOrderByCreatedAtDesc(
                ReportStatus.PENDING.getValue()
        );
    }

    @Test
    void testDelete() {
        doNothing().when(reportRepository).delete(any(Report.class));

        reportRepository.delete(report1);

        verify(reportRepository).delete(report1);
    }

    @Test
    void testDeleteById() {
        doNothing().when(reportRepository).deleteById(any(UUID.class));

        reportRepository.deleteById(uuid1);

        verify(reportRepository).deleteById(uuid1);
    }
}