package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;

import java.util.List;
import java.util.UUID;

public interface ReportService {

    Report createReport(Report report);

    void deleteReport(UUID reportId);

    Report updateStatusReport(UUID reportId, String newStatus);

    List<Report> findAllReport();

    List<Report> findReportByUser(String userId);

    Report findReportById(UUID reportId);


}