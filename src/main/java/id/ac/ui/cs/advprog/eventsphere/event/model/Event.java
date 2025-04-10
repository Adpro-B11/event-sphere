package id.ac.ui.cs.advprog.eventsphere.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    private String status = "DRAFT"; // Default status

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public void setStatus(String status) {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }

    private boolean isValidStatus(String status) {
        return status.equals("DRAFT") || status.equals("PUBLISHED") || status.equals("CANCELLED");
    }
}