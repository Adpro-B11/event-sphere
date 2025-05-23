package id.ac.ui.cs.advprog.eventsphere.report.service;

import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import id.ac.ui.cs.advprog.eventsphere.report.repository.ReportMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportMessageServiceImpl implements ReportMessageService {

    private final ReportMessageRepository reportMessageRepository;
    private final ReportService reportService;

    @Override
    public ReportMessage createMessage(ReportMessage message) {
        return reportMessageRepository.save(message);
    }

    @Override
    public ReportMessage createMessageFromDTO(UUID reportID, String message, String sender) {
        Report report = reportService.findReportById(reportID);

        ReportMessage newMessage = new ReportMessage(report, message, sender);

        return createMessage(newMessage);
    }

    @Override
    public List<ReportMessage> findMessagesByReportId(UUID reportID) {
        return reportMessageRepository.findByReportReportID(reportID);
    }

    @Override
    public ReportMessage findMessageById(UUID messageID) {
        return reportMessageRepository.findById(messageID)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }
}