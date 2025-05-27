package id.ac.ui.cs.advprog.eventsphere.event.enums;

import lombok.Getter;

@Getter
public enum EventStatus {
    DRAFT("DRAFT"),
    PUBLISHED("PUBLISHED"),
    CANCELLED("CANCELLED");

    private final String value;

    private EventStatus(String value){
        this.value = value;
    }

    public static boolean contains(String status) {
        for (EventStatus eventStatus : values()) {
            if (eventStatus.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
}