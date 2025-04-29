package id.ac.ui.cs.advprog.eventsphere.report.observer;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;

/**
 * Observer interface for the Report model.
 * Implementations will be notified when a report's status or response changes.
 */
public interface ReportObserver {
    void update(Report report);
}