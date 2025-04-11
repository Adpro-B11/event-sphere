package id.ac.ui.cs.advprog.eventsphere.report.enums;

public enum ReportStatus {
    ON_PROGRESS("ON_PROGRESS"),
    RESOLVED("RESOLVED");

    private final String status;

    private ReportStatus(String status) {
        this.status = status;
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
