package id.ac.ui.cs.advprog.eventsphere.report.enums;

import lombok.Getter;

@Getter
public enum ReportCategory {
    PAYMENT("PAYMENT"),
    TICKET("TICKET"),
    EVENT_ISSUE("EVENT_ISSUE");

    private final String value;

    private ReportCategory(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (ReportCategory reportCategory : ReportCategory.values()) {
            if (reportCategory.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
