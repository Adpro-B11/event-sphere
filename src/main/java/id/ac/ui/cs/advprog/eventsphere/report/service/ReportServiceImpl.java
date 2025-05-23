package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public List<Report> findReportByUser(String UserID) {
        return reportRepository.findByCreatedBy(UserID) ;
    }

    @Override
    public Report findReportById(UUID reportId) {
        return reportRepository.findById(reportId).orElseThrow(() -> new IllegalArgumentException("Report not found"));
    }
}