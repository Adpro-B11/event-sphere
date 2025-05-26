package id.ac.ui.cs.advprog.eventsphere.report.controller;

import id.ac.ui.cs.advprog.eventsphere.report.dto.CreateReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.mapper.ReportMessageMapper;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import id.ac.ui.cs.advprog.eventsphere.report.service.ReportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller responsible for handling API endpoints related to report messages.
 * Manages operations for creating and retrieving messages associated with reports.
 */
@RestController
@RequestMapping("api/report-messages")
@RequiredArgsConstructor
public class ReportMessageController {

    private final ReportMessageService reportMessageService;
    private final ReportMessageMapper reportMessageMapper;

    /**
     * Creates a new message for a specific report.
     *
     * @param createReportMessageDTO DTO containing report ID, message content, and sender information
     * @return ResponseEntity containing the created message with HTTP status 201 (CREATED)
     */
    @PostMapping("/new")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<ReportMessageDTO> createMessage(@RequestBody CreateReportMessageDTO createReportMessageDTO) {
        ReportMessage message = reportMessageService.createMessageFromDTO(
                createReportMessageDTO.getReportID(),
                createReportMessageDTO.getMessage(),
                createReportMessageDTO.getSender()
        );
        return new ResponseEntity<>(reportMessageMapper.toReportMessageDTO(message), HttpStatus.CREATED);
    }

    /**
     * Retrieves all messages associated with a specific report.
     *
     * @param reportId The unique identifier of the report whose messages to retrieve
     * @return ResponseEntity containing a list of messages for the specified report
     */
    @GetMapping("/report/{reportId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<List<ReportMessageDTO>> getMessagesByReportId(@PathVariable("reportId") UUID reportId) {
        List<ReportMessage> messages = reportMessageService.findMessagesByReportId(reportId);
        return ResponseEntity.ok(reportMessageMapper.toReportMessageDTOs(messages));
    }

    /**
     * Retrieves a specific message by its unique identifier.
     *
     * @param messageId The unique identifier of the message to retrieve
     * @return ResponseEntity containing the requested message details
     */
    @GetMapping("/{messageId}")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    public ResponseEntity<ReportMessageDTO> getMessageById(@PathVariable("messageId") UUID messageId) {
        ReportMessage message = reportMessageService.findMessageById(messageId);
        return ResponseEntity.ok(reportMessageMapper.toReportMessageDTO(message));
    }
}
