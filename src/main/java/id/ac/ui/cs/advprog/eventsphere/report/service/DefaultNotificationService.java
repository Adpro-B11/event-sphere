package id.ac.ui.cs.advprog.eventsphere.report.service;

import org.springframework.stereotype.Service;

/**
 * Default implementation of NotificationService.
 * In a real application, this would connect to a messaging system.
 */
@Service
public class DefaultNotificationService implements NotificationService {
    
    @Override
    public void sendNotification(String userId, String message) {
        // Sementara untuk check fungsi secara umum
        System.out.println("Notification sent to user " + userId + ": " + message);
    }
}
