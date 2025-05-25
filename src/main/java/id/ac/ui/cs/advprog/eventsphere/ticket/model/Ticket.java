package id.ac.ui.cs.advprog.eventsphere.ticket.model;

import id.ac.ui.cs.advprog.eventsphere.ticket.enums.TicketType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@NoArgsConstructor
@Getter
public class Ticket {

    @Id
    @Setter
    private String id = UUID.randomUUID().toString();

    @Column(name = "event_id", nullable = false)
    @Setter
    private String eventId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType type;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quota;

    @Column(nullable = false)
    private int remaining;

    @Column(name = "is_active", nullable = false)
    @Setter
    private boolean isActive = true;

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Ticket price cannot be negative");
        }
        this.price = price;
    }

    public void setQuota(int quota) {
        if (quota < 0) {
            throw new IllegalArgumentException("Quota cannot be negative");
        }
        this.quota = quota;
    }

    public void setRemaining(int remaining) {
        if (remaining < 0 || remaining > this.quota) {
            throw new IllegalArgumentException("Remaining tickets must be between 0 and quota");
        }
        this.remaining = remaining;
    }

    public void setType(TicketType type) {
        if (type == null) {
            throw new IllegalArgumentException("Ticket type cannot be null");
        }
        this.type = type;
    }
}
