package id.ac.ui.cs.advprog.eventsphere.event.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class Event {
    private String id = UUID.randomUUID().toString();
    private String title;
    private String description;
    private String date;
    private String location;
    private double price;
    private String status = "DRAFT"; // Default status
}