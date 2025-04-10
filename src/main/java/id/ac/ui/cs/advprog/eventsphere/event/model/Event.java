// src/main/java/model/Event.java
package id.ac.ui.cs.advprog.eventsphere.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;
import id.ac.ui.cs.advprog.eventsphere.event.enums.EventStatus;

@NoArgsConstructor
@Getter
public class Event {
    private String id = UUID.randomUUID().toString();

    @Setter
    private String title;

    @Setter
    private String description;

    @Setter
    private String date;

    @Setter
    private String location;

    @Setter
    private double price;

    private String status = EventStatus.DRAFT.getValue(); // Default status

    @Setter
    private String organizer;

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public void setDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty");
        }
        this.date = date;
    }

    public void setLocation(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public void setStatus(String status) {
        if (!EventStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }
}