package id.ac.ui.cs.advprog.eventsphere.report.observer;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.service.NotificationService;
import org.springframework.stereotype.Component;

/**
 * An implementation of ReportObserver that sends notifications when reports are updated.
 */
@Component
public class NotificationReportObserver implements ReportObserver {

    private final NotificationService notificationService;

    public NotificationReportObserver() {
    }

    @Override
    public void update(Report report) {
    }

    private String generateNotificationMessage(Report report) {

    }
}

