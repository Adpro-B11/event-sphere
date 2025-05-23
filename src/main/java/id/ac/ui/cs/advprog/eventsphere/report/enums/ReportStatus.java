package id.ac.ui.cs.advprog.eventsphere.report.enums;

import lombok.Getter;

@Getter
public enum ReportStatus {
    PENDING("PENDING"),
    ON_PROGRESS("ON PROGRESS"),
    RESOLVED("RESOLVED");

    private final String value;

    private ReportStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (ReportStatus reportStatus : ReportStatus.values()) {
            if (reportStatus.name().equals(param)) {
                return true;
            }
        }
        return false;
    }



}
