package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;

import java.util.List;
import java.util.UUID;

public interface ReportMessageService {
    ReportMessage createMessage(ReportMessage message);

    ReportMessage createMessageFromDTO(UUID reportID, String message, String sender);

    List<ReportMessage> findMessagesByReportId(UUID reportID);

    ReportMessage findMessageById(UUID messageID);

}