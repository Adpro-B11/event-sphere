//package id.ac.ui.cs.advprog.eventsphere.report.observer;
//
//import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
//import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
//import id.ac.ui.cs.advprog.eventsphere.report.service.NotificationService;
//import org.springframework.stereotype.Component;
//
///**
// * An implementation of ReportObserver that sends notifications when reports are updated.
// */
//@Component
//public class NotificationReportObserver implements ReportObserver {
//
//    private final NotificationService notificationService;
//
//    public NotificationReportObserver(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    @Override
//    public void update(Report report) {
//        String message = generateNotificationMessage(report);
//        notificationService.sendNotification(report.getAttendeeId(), message);
//    }
//
//    private String generateNotificationMessage(Report report) {
//        StringBuilder message = new StringBuilder("Your report ");
//
//        if (report.getStatus() == ReportStatus.RESOLVED.getValue()) {
//            message.append("has been resolved");
//            if (report.getResponseMessage() != null && !report.getResponseMessage().isEmpty()) {
//                message.append(" with the following response: ").append(report.getResponseMessage());
//            }
//        } else {
//            message.append("status has been updated to ").append(report.getStatus());
//        }
//
//        return message.toString();
//    }
//}
//
