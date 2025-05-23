package id.ac.ui.cs.advprog.eventsphere.report.factory;

import id.ac.ui.cs.advprog.eventsphere.report.dto.CreateReportDTO;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportFactory {

    private final ReportService ReportService;

    // Factory method
    public Report createReport(CreateReportDTO newReport) {
        String title = newReport.getTitle();
        String description = newReport.getDescription();
        String category = newReport.getCategory();
        String categoryReference = newReport.getCategoryReference();
        String createdBy = newReport.getCreatedBy();

        validateInput(title, description, category, createdBy);
        validateReference(category, categoryReference);

        Report report = new Report(title, description, category.toUpperCase(), categoryReference, createdBy);
        return ReportService.createReport(report);
    }

    private void validateInput(String title, String description , String category, String createdBy) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title report can't be empty");
        }

        if (title.length() > 35) {
            throw new IllegalArgumentException("Title report cannot be more than 35 characters");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description report can't be empty");
        }

        if (description.length() > 100) {
            throw new IllegalArgumentException("Description report cannot be more than 100 characters");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category report can't be null");
        }

        if (!ReportCategory.contains(category)) {
            throw new IllegalArgumentException("Category report not valid");
        }

        if (createdBy == null || createdBy.trim().isEmpty()) {
            throw new IllegalArgumentException("Report creator can't be empty");
        }
    }

    private void validateReference(String strCategory, String categoryReference) {

        ReportCategory category = ReportCategory.valueOf(strCategory.toUpperCase());

        // [TODO] Implementasi pengecekan referensi disetiap case dengan memanggil masing-masing repo
        boolean exist = true; // Untuk simulasi saja

        switch (category) {
            case OTHER:
                if (categoryReference != null && !categoryReference.trim().isEmpty()) {
                    throw new IllegalArgumentException("No need Reference for category: " + category.getValue());
                }
                break;
            case EVENT_ISSUE:
                if (!exist) {
                    throw new IllegalArgumentException("Event not valid: " + categoryReference);
                }
                break;
            case TICKET:
                if (!exist) {
                    throw new IllegalArgumentException("Ticket not valid: " + categoryReference);
                }
                break;
            case PAYMENT:
                if (!exist) {
                    throw new IllegalArgumentException("Payment not valid: " + categoryReference);
                }
                break;
            default:
                throw new IllegalArgumentException("Category report not valid");
        }
    }
}
