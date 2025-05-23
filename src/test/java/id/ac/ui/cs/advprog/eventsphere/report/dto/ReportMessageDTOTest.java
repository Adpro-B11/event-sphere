package id.ac.ui.cs.advprog.eventsphere.report.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportMessageDTOTest {

    @Test
    void testNoArgsConstructor() {
        // Act
        ReportMessageDTO dto = new ReportMessageDTO();

        // Assert
        assertNull(dto.getMessageID());
        assertNull(dto.getMessage());
        assertNull(dto.getTimestamp());
        assertNull(dto.getSender());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        UUID messageID = UUID.randomUUID();
        String message = "Bagaimana status laporan saya?";
        LocalDateTime timestamp = LocalDateTime.now();
        String sender = "user123";

        // Act
        ReportMessageDTO dto = new ReportMessageDTO(messageID, message, timestamp, sender);

        // Assert
        assertEquals(messageID, dto.getMessageID());
        assertEquals(message, dto.getMessage());
        assertEquals(timestamp, dto.getTimestamp());
        assertEquals(sender, dto.getSender());
    }

    @Test
    void testBuilder() {
        // Arrange
        UUID messageID = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        ReportMessageDTO dto = ReportMessageDTO.builder()
                .messageID(messageID)
                .message("Mohon tunggu, kami sedang memeriksa masalah Anda")
                .timestamp(timestamp)
                .sender("admin")
                .build();

        // Assert
        assertEquals(messageID, dto.getMessageID());
        assertEquals("Mohon tunggu, kami sedang memeriksa masalah Anda", dto.getMessage());
        assertEquals(timestamp, dto.getTimestamp());
        assertEquals("admin", dto.getSender());
    }

    @Test
    void testSetterGetter() {
        // Arrange
        ReportMessageDTO dto = new ReportMessageDTO();
        UUID messageID = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        dto.setMessageID(messageID);
        dto.setMessage("Terima kasih atas responnya");
        dto.setTimestamp(timestamp);
        dto.setSender("user456");

        // Assert
        assertEquals(messageID, dto.getMessageID());
        assertEquals("Terima kasih atas responnya", dto.getMessage());
        assertEquals(timestamp, dto.getTimestamp());
        assertEquals("user456", dto.getSender());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID messageID = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        ReportMessageDTO dto1 = ReportMessageDTO.builder()
                .messageID(messageID)
                .message("Message")
                .timestamp(timestamp)
                .sender("user")
                .build();

        ReportMessageDTO dto2 = ReportMessageDTO.builder()
                .messageID(messageID)
                .message("Message")
                .timestamp(timestamp)
                .sender("user")
                .build();

        ReportMessageDTO dto3 = ReportMessageDTO.builder()
                .messageID(UUID.randomUUID())
                .message("Different Message")
                .timestamp(LocalDateTime.now().plusHours(1))
                .sender("different-user")
                .build();

        // Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, dto3);
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        UUID messageID = UUID.randomUUID();
        String message = "Test Message";

        ReportMessageDTO dto = ReportMessageDTO.builder()
                .messageID(messageID)
                .message(message)
                .build();

        // Act
        String toStringResult = dto.toString();

        // Assert
        assertTrue(toStringResult.contains(messageID.toString()));
        assertTrue(toStringResult.contains(message));
    }
}
