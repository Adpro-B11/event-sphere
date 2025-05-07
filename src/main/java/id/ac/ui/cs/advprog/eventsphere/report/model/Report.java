package id.ac.ui.cs.advprog.eventsphere.report.model;

import jakarta.persistence.*;
import lombok.*;

import id.ac.ui.cs.advprog.eventsphere.report.enums.ReportStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "messages")
@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID reportID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(name = "category_reference")
    private String categoryReference;

    @Column(name = "attachment_path")
    private String attachmentPath;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    // Relasi One-to-Many dengan ReportMessage
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportMessage> messages = new ArrayList<>();

    // Constructor yang digunakan oleh ReportFactory
    public Report(String title, String description, String category, String categoryReference, String attachmentPath, String createdBy) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.categoryReference = categoryReference;
        this.attachmentPath = attachmentPath;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.status = ReportStatus.PENDING.getValue();
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
    }

    public void addMessage(String text, String sender) {
        ReportMessage message = new ReportMessage(this, text, sender);
        this.messages.add(message);
    }
}
