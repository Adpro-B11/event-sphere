package id.ac.ui.cs.advprog.eventsphere.report.mapper;

import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for conversion between ReportMessage entity and DTO
 */
@Component
public class ReportMessageMapper {

    /**
     * Convert ReportMessage entity to ReportMessageDTO
     */
    public ReportMessageDTO toReportMessageDTO(ReportMessage message) {
        if (message == null) {
            return null;
        }

        return ReportMessageDTO.builder()
                .messageID(message.getMessageID())
                .message(message.getMessage())
                .timestamp(message.getTimestamp())
                .sender(message.getSender())
                .build();
    }

    /**
     * Convert list of ReportMessage to list of ReportMessageDTO
     */
    public List<ReportMessageDTO> toReportMessageDTOs(List<ReportMessage> messages) {
        if (messages == null) {
            return Collections.emptyList();
        }

        return messages.stream()
                .map(this::toReportMessageDTO)
                .collect(Collectors.toList());
    }
}
