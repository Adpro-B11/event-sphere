package id.ac.ui.cs.advprog.eventsphere.report.service;

/**
 * Service for sending notifications to users.
 */
public interface NotificationService {
    void sendNotification(String userId, String message);
}
