package id.ac.ui.cs.advprog.eventsphere.report.repository;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    // Cari laporan berdasarkan status
    List<Report> findByStatus(String status);

    // Cari laporan berdasarkan kategori
    List<Report> findByCategory(String category);

    // Cari laporan berdasarkan pembuat laporan
    List<Report> findByCreatedBy(String createdBy);

    // Cari laporan berdasarkan tanggal pembuatan (range)
    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    // Cari laporan berdasarkan judul yang mengandung kata tertentu
    List<Report> findByTitleContainingIgnoreCase(String keyword);

    // Cari laporan berdasarkan referensi kategori (ID acara, tiket, pembayaran)
    List<Report> findByCategoryReference(String categoryReference);

    // Kombinasi filter - kategori dan status
    List<Report> findByCategoryAndStatus(String category, String status);

    // Kombinasi filter - pembuat dan status
    List<Report> findByCreatedByAndStatus(String createdBy, String status);

    // Cari laporan dengan urutan dari yang terbaru
    List<Report> findAllByOrderByCreatedAtDesc();

    // Cari laporan berdasarkan kategori dengan urutan terbaru
    List<Report> findByCategoryOrderByCreatedAtDesc(String category);

    // Cari laporan berdasarkan status dengan urutan terbaru
    List<Report> findByStatusOrderByCreatedAtDesc(String status);
}
