package id.ac.ui.cs.advprog.eventsphere.report.model;

import id.ac.ui.cs.advprog.eventsphere.report.observer.ReportObserver;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;
import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class Report {
    private String reportId;
    private String attendeeId;

    @Setter
    private String eventId;

    @Setter
    private String ticketId;

    private String description;

    private String category;

    private String status;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    @Setter
    private List<String> attachments;

    private String responseMessage;

    private List<ReportObserver> observers = new ArrayList<>();


    public Report(String reportId, String attendeeId, String eventId, String ticketId,
                  String description, String category, String status,
                  LocalDateTime createdAt, LocalDateTime updatedAt,
                  List<String> attachments, String responseMessage) {

        if (attendeeId == null || attendeeId.isEmpty()) {
            throw new IllegalArgumentException("Attendee ID cannot be null or empty");
        }

        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        if (category == null || !ReportCategory.contains(category)) {
            throw new IllegalArgumentException("Report category cannot be null or invalid");
        }

        if (status == null) {
            throw new IllegalArgumentException("Report status cannot be null");
        }

        this.reportId = reportId != null ? reportId : UUID.randomUUID().toString();
        this.attendeeId = attendeeId;
        this.eventId = eventId;
        this.ticketId = ticketId;
        this.description = description;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt;
        this.attachments = attachments;
        this.responseMessage = responseMessage;
    }

    // Factory method for creating a new report
    public static Report createNewReport(String attendeeId, String eventId, String ticketId,
                                         String description, String category,
                                         List<String> attachments) {
        return new Report(
                UUID.randomUUID().toString(),
                attendeeId,
                eventId,
                ticketId,
                description,
                category,
                ReportStatus.ON_PROGRESS.getValue(),
                LocalDateTime.now(),
                null,
                attachments,
                null
        );
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description;
    }

    public void setCategory(String category) {
        if (category == null || !ReportCategory.contains(category)) {
            throw new IllegalArgumentException("Report category cannot be null or invalid");
        }
        this.category = category;
    }

    public void setStatus(String status) {
        if (status == null || !ReportStatus.contains(status)) {
            throw new IllegalArgumentException("Report status cannot be null or invalid");
        }

        String oldStatus = this.status;
        this.status = status;
        this.updatedAt = LocalDateTime.now();

        if (!status.equals(oldStatus)) {
            notifyObservers();
        }

    }

    public void setResponseMessage(String responseMessage) {
        String oldMessage = this.responseMessage;
        this.responseMessage = responseMessage;
        this.updatedAt = LocalDateTime.now();

        if ((oldMessage == null && responseMessage != null) ||
                (oldMessage != null && !oldMessage.equals(responseMessage))) {
            notifyObservers();
        }
    }

    // Method to add response and update status
    public void resolveReport(String responseMessage) {
        this.responseMessage = responseMessage;
        String oldStatus = this.status;
        this.status = ReportStatus.RESOLVED.getValue();
        this.updatedAt = LocalDateTime.now();

        // Always notify observers when resolving a report
        notifyObservers();
    }

    // Method to add attachment
    public void addAttachment(String attachmentUrl) {
        if (this.attachments == null) {
            throw new IllegalStateException("Attachments list is not initialized");
        }
        this.attachments.add(attachmentUrl);
        this.updatedAt = LocalDateTime.now();
    }

    // Observer pattern methods
    public void addObserver(ReportObserver observer) {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        observers.add(observer);
    }

    public void removeObserver(ReportObserver observer) {
        if (observers != null) {
            observers.remove(observer);
        }
    }

    private void notifyObservers() {
        if (observers != null) {
            for (ReportObserver observer : observers) {
                observer.update(this);
            }
        }
    }
}
