package id.ac.ui.cs.advprog.eventsphere.report.repository;

import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportMessageRepository extends JpaRepository<ReportMessage, UUID> {
    List<ReportMessage> findByReportReportID(UUID reportID);
}