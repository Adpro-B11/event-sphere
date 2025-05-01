package id.ac.ui.cs.advprog.eventsphere.ticket.enums;

public enum TicketType {
    VIP("VIP", "Akses penuh dengan fasilitas premium"),
    REGULAR("Regular", "Akses standar ke acara"),
    EARLY_BIRD("Early Bird", "Tiket regular dengan harga diskon"),
    STUDENT("Student", "Tiket khusus untuk pelajar/mahasiswa dengan diskon"),
    GROUP("Group", "Tiket untuk group dengan minimal 5 orang");

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
