package id.ac.ui.cs.advprog.eventsphere.report.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "report_messages")
@EqualsAndHashCode(exclude = "report")
@ToString(exclude = "report")
@NoArgsConstructor
@AllArgsConstructor
public class ReportMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String sender;

    public ReportMessage(Report report, String message, String sender) {
        if (report == null) {
            throw new IllegalArgumentException("Report cannot be null");
        }
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
        if (sender == null || sender.trim().isEmpty()) {
            throw new IllegalArgumentException("Sender cannot be empty");
        }
        this.report = report;
        this.message = message;
        this.sender = sender;
        this.timestamp = LocalDateTime.now();

    }
}
