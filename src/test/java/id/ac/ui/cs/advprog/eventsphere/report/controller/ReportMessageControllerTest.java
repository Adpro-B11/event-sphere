package id.ac.ui.cs.advprog.eventsphere.report.controller;

import id.ac.ui.cs.advprog.eventsphere.report.dto.CreateReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.dto.ReportMessageDTO;
import id.ac.ui.cs.advprog.eventsphere.report.mapper.ReportMessageMapper;
import id.ac.ui.cs.advprog.eventsphere.report.model.Report;
import id.ac.ui.cs.advprog.eventsphere.report.model.ReportMessage;
import id.ac.ui.cs.advprog.eventsphere.report.service.ReportMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportMessageControllerTest {

    @Mock
    private ReportMessageService reportMessageService;

    @Mock
    private ReportMessageMapper reportMessageMapper;

    @InjectMocks
    private ReportMessageController reportMessageController;

    private MockMvc mockMvc;
    private ReportMessage reportMessage;
    private ReportMessageDTO reportMessageDTO;
    private UUID reportId;
    private UUID messageId;
    private String sender;
    private String messageText;
    private Report report;
    private List<ReportMessage> messageList;
    private List<ReportMessageDTO> messageDTOList;
    private CreateReportMessageDTO createReportMessageDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reportMessageController).build();

        reportId = UUID.randomUUID();
        messageId = UUID.randomUUID();
        sender = "user123";
        messageText = "Mohon bantuannya untuk masalah tiket saya";

        report = new Report(
                "Tiket tidak muncul",
                "Tiket tidak muncul setelah pembayaran",
                "TICKET",
                "TICKET-123",
                "path/to/attachment",
                "user123"
        );

        reportMessage = new ReportMessage(report, messageText, sender);

        // Refleksi untuk mengatur messageID karena field tersebut final dan tidak dapat diubah langsung
        try {
            java.lang.reflect.Field messageIDField = ReportMessage.class.getDeclaredField("messageID");
            messageIDField.setAccessible(true);
            messageIDField.set(reportMessage, messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        reportMessageDTO = ReportMessageDTO.builder()
                .messageID(messageId)
                .message(messageText)
                .timestamp(LocalDateTime.now())
                .sender(sender)
                .build();

        messageList = Collections.singletonList(reportMessage);
        messageDTOList = Collections.singletonList(reportMessageDTO);

        createReportMessageDTO = new CreateReportMessageDTO(reportId, sender, messageText);
    }

    @Test
    void createMessage_ShouldReturnCreatedMessage() {
        when(reportMessageService.createMessageFromDTO(reportId, messageText, sender)).thenReturn(reportMessage);
        when(reportMessageMapper.toReportMessageDTO(reportMessage)).thenReturn(reportMessageDTO);

        ResponseEntity<ReportMessageDTO> response = reportMessageController.createMessage(createReportMessageDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reportMessageDTO, response.getBody());
        verify(reportMessageService, times(1)).createMessageFromDTO(reportId, messageText, sender);
        verify(reportMessageMapper, times(1)).toReportMessageDTO(reportMessage);
    }

    @Test
    void getMessagesByReportId_ShouldReturnAllMessagesForReport() {
        when(reportMessageService.findMessagesByReportId(reportId)).thenReturn(messageList);
        when(reportMessageMapper.toReportMessageDTOs(messageList)).thenReturn(messageDTOList);

        ResponseEntity<List<ReportMessageDTO>> response = reportMessageController.getMessagesByReportId(reportId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageDTOList, response.getBody());
        verify(reportMessageService, times(1)).findMessagesByReportId(reportId);
        verify(reportMessageMapper, times(1)).toReportMessageDTOs(messageList);
    }

    @Test
    void getMessageById_ShouldReturnMessage() {
        when(reportMessageService.findMessageById(messageId)).thenReturn(reportMessage);
        when(reportMessageMapper.toReportMessageDTO(reportMessage)).thenReturn(reportMessageDTO);

        ResponseEntity<ReportMessageDTO> response = reportMessageController.getMessageById(messageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reportMessageDTO, response.getBody());
        verify(reportMessageService, times(1)).findMessageById(messageId);
        verify(reportMessageMapper, times(1)).toReportMessageDTO(reportMessage);
    }
}