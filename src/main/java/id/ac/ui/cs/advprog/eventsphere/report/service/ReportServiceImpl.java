package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public void deleteReport(UUID reportId) {
        Report  report = findReportById(reportId);
        reportRepository.delete(report);
    }

    @Override
    public Report updateStatusReport(UUID reportId, String newStatus) {
        Report existingReport = findReportById(reportId);

        if (!ReportStatus.contains(newStatus)) {
            throw new IllegalArgumentException("Status report not valid");
        }

        if (existingReport.getStatus().equals(newStatus)) {
            throw new IllegalArgumentException("Status report already set to " + newStatus);
        }

        existingReport.updateStatus(newStatus);

        return reportRepository.save(existingReport);
    }

    @Override
    public List<Report> findAllReport() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> findReportByUser(String userId) {
        return reportRepository.findByCreatedBy(userId) ;
    }

    @Override
    public Report findReportById(UUID reportId) {
        return reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }

    /**
     * Async version untuk fetch all reports
     * Menggunakan taskExecutor yang sudah dikonfigurasi teman Anda
     */
    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<Report>> findAllReportAsync() {
        try {
            List<Report> reports = reportRepository.findAll();
            return CompletableFuture.completedFuture(reports);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * Async version untuk fetch reports by user
     * Menggunakan taskExecutor yang sudah dikonfigurasi teman Anda
     */
    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<Report>> findReportByUserAsync(String userId) {
        try {
            List<Report> reports = reportRepository.findByCreatedBy(userId);
            return CompletableFuture.completedFuture(reports);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

}