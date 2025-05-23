package id.ac.ui.cs.advprog.eventsphere.report.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportDetailDTOTest {

    @Test
    void testNoArgsConstructor() {
        // Act
        ReportDetailDTO dto = new ReportDetailDTO();

        // Assert
        assertNull(dto.getReportID());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getCategory());
        assertNull(dto.getCategoryReference());
        assertNull(dto.getStatus());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getCreatedBy());
        assertNull(dto.getMessages());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        UUID reportID = UUID.randomUUID();
        String title = "Pembayaran Gagal";
        String description = "Pembayaran untuk tiket event tidak berhasil";
        String category = "PAYMENT";
        String categoryReference = "TRX-123";
        String status = "PENDING";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "user123";
        List<ReportMessageDTO> messages = new ArrayList<>();

        // Act
        ReportDetailDTO dto = new ReportDetailDTO(
                reportID, title, description, category, categoryReference,
                status, createdAt, createdBy, messages
        );

        // Assert
        assertEquals(reportID, dto.getReportID());
        assertEquals(title, dto.getTitle());
        assertEquals(description, dto.getDescription());
        assertEquals(category, dto.getCategory());
        assertEquals(categoryReference, dto.getCategoryReference());
        assertEquals(status, dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(createdBy, dto.getCreatedBy());
        assertEquals(messages, dto.getMessages());
    }

    @Test
    void testBuilder() {
        // Arrange
        UUID reportID = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        List<ReportMessageDTO> messages = new ArrayList<>();
        messages.add(new ReportMessageDTO(UUID.randomUUID(), "Pesan bantuan", LocalDateTime.now(), "user123"));

        // Act
        ReportDetailDTO dto = ReportDetailDTO.builder()
                .reportID(reportID)
                .title("Event Tidak Muncul")
                .description("Event yang saya buat tidak muncul di halaman utama")
                .category("EVENT_ISSUE")
                .categoryReference("EVENT-456")
                .status("ON_PROGRESS")
                .createdAt(createdAt)
                .createdBy("user456")
                .messages(messages)
                .build();

        // Assert
        assertEquals(reportID, dto.getReportID());
        assertEquals("Event Tidak Muncul", dto.getTitle());
        assertEquals("Event yang saya buat tidak muncul di halaman utama", dto.getDescription());
        assertEquals("EVENT_ISSUE", dto.getCategory());
        assertEquals("EVENT-456", dto.getCategoryReference());
        assertEquals("ON_PROGRESS", dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals("user456", dto.getCreatedBy());
        assertEquals(messages, dto.getMessages());
    }

    @Test
    void testSetterGetter() {
        // Arrange
        ReportDetailDTO dto = new ReportDetailDTO();
        UUID reportID = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        List<ReportMessageDTO> messages = new ArrayList<>();

        // Act
        dto.setReportID(reportID);
        dto.setTitle("Tiket Duplikat");
        dto.setDescription("Saya mendapatkan tiket duplikat");
        dto.setCategory("TICKET");
        dto.setCategoryReference("TIX-789");
        dto.setStatus("RESOLVED");
        dto.setCreatedAt(createdAt);
        dto.setCreatedBy("user789");
        dto.setMessages(messages);

        // Assert
        assertEquals(reportID, dto.getReportID());
        assertEquals("Tiket Duplikat", dto.getTitle());
        assertEquals("Saya mendapatkan tiket duplikat", dto.getDescription());
        assertEquals("TICKET", dto.getCategory());
        assertEquals("TIX-789", dto.getCategoryReference());
        assertEquals("RESOLVED", dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals("user789", dto.getCreatedBy());
        assertEquals(messages, dto.getMessages());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID reportID = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        List<ReportMessageDTO> messages = new ArrayList<>();

        ReportDetailDTO dto1 = ReportDetailDTO.builder()
                .reportID(reportID)
                .title("Title")
                .description("Description")
                .category("PAYMENT")
                .categoryReference("REF-123")
                .status("PENDING")
                .createdAt(createdAt)
                .createdBy("user")
                .messages(messages)
                .build();

        ReportDetailDTO dto2 = ReportDetailDTO.builder()
                .reportID(reportID)
                .title("Title")
                .description("Description")
                .category("PAYMENT")
                .categoryReference("REF-123")
                .status("PENDING")
                .createdAt(createdAt)
                .createdBy("user")
                .messages(messages)
                .build();

        ReportDetailDTO dto3 = ReportDetailDTO.builder()
                .reportID(UUID.randomUUID())
                .title("Different Title")
                .description("Different Description")
                .category("TICKET")
                .categoryReference("REF-456")
                .status("RESOLVED")
                .createdAt(LocalDateTime.now().plusDays(1))
                .createdBy("another-user")
                .messages(new ArrayList<>())
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
        UUID reportID = UUID.randomUUID();
        String title = "Test Report";

        ReportDetailDTO dto = ReportDetailDTO.builder()
                .reportID(reportID)
                .title(title)
                .build();

        // Act
        String toStringResult = dto.toString();

        // Assert
        assertTrue(toStringResult.contains(reportID.toString()));
        assertTrue(toStringResult.contains(title));
    }
}
