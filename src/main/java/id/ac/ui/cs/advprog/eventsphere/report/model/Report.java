package id.ac.ui.cs.advprog.eventsphere.report.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Report {
    private String reportId;
    private String attendeeId;
    private String eventId;
    private String ticketId;
    private String description;
    private String category;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> attachments;
    private String responseMessage;

    public Report(String reportId, String attendeeId, String eventId, String ticketId,
                  String description, String category, String status,
                  LocalDateTime createdAt, LocalDateTime updatedAt,
                  List<String> attachments, String responseMessage) {
    }
}