package id.ac.ui.cs.advprog.eventsphere.ticket.enums;

public enum TicketType {
    VIP("VIP", "Full access with premium facilities"),
    REGULAR("Regular", "Standard access to the event"),
    EARLY_BIRD("Early Bird", "Regular ticket at a discounted price"),
    STUDENT("Student", "Special ticket for students with a discount"),
    GROUP("Group", "Ticket for groups with a minimum of 5 people");

    private final String name;
    private final String description;

    TicketType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
