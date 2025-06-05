package id.ac.ui.cs.advprog.eventsphere.report.repository;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        uuid1 = UUID.randomUUID();
        uuid2 = UUID.randomUUID();

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
    void testDelete() {
        doNothing().when(reportRepository).delete(any(Report.class));

        reportRepository.delete(report1);

        verify(reportRepository).delete(report1);
    }
}
