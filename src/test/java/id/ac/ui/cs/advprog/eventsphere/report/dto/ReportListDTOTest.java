package id.ac.ui.cs.advprog.eventsphere.report.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportListDTOTest {

    @Test
    void testNoArgsConstructor() {
        // Act
        ReportListDTO dto = new ReportListDTO();

        // Assert
        assertNull(dto.getReportID());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getCategory());
        assertNull(dto.getStatus());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getCreatedBy());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        UUID reportID = UUID.randomUUID();
        String title = "Pembayaran Gagal";
        String description = "Pembayaran tiket gagal";
        String category = "PAYMENT";
        String status = "PENDING";
        LocalDateTime createdAt = LocalDateTime.now();
        String createdBy = "user123";

        // Act
        ReportListDTO dto = new ReportListDTO(
                reportID, title, description, category, status, createdAt, createdBy
        );

        // Assert
        assertEquals(reportID, dto.getReportID());
        assertEquals(title, dto.getTitle());
        assertEquals(description, dto.getDescription());
        assertEquals(category, dto.getCategory());
        assertEquals(status, dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(createdBy, dto.getCreatedBy());
    }

    @Test
    void testBuilder() {
        // Arrange
        UUID reportID = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        ReportListDTO dto = ReportListDTO.builder()
                .reportID(reportID)
                .title("Acara Dibatalkan")
                .description("Acara yang sudah saya beli tiketnya dibatalkan")
                .category("EVENT_ISSUE")
                .status("ON_PROGRESS")
                .createdAt(createdAt)
                .createdBy("user456")
                .build();

        // Assert
        assertEquals(reportID, dto.getReportID());
        assertEquals("Acara Dibatalkan", dto.getTitle());
        assertEquals("Acara yang sudah saya beli tiketnya dibatalkan", dto.getDescription());
        assertEquals("EVENT_ISSUE", dto.getCategory());
        assertEquals("ON_PROGRESS", dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals("user456", dto.getCreatedBy());
    }

    @Test
    void testSetterGetter() {
        // Arrange
        ReportListDTO dto = new ReportListDTO();
        UUID reportID = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        dto.setReportID(reportID);
        dto.setTitle("Tiket Hilang");
        dto.setDescription("Tiket saya hilang dari daftar");
        dto.setCategory("TICKET");
        dto.setStatus("RESOLVED");
        dto.setCreatedAt(createdAt);
        dto.setCreatedBy("user789");

        // Assert
        assertEquals(reportID, dto.getReportID());
        assertEquals("Tiket Hilang", dto.getTitle());
        assertEquals("Tiket saya hilang dari daftar", dto.getDescription());
        assertEquals("TICKET", dto.getCategory());
        assertEquals("RESOLVED", dto.getStatus());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals("user789", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID reportID = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();

        ReportListDTO dto1 = ReportListDTO.builder()
                .reportID(reportID)
                .title("Title")
                .description("Description")
                .category("PAYMENT")
                .status("PENDING")
                .createdAt(createdAt)
                .createdBy("user")
                .build();

        ReportListDTO dto2 = ReportListDTO.builder()
                .reportID(reportID)
                .title("Title")
                .description("Description")
                .category("PAYMENT")
                .status("PENDING")
                .createdAt(createdAt)
                .createdBy("user")
                .build();

        ReportListDTO dto3 = ReportListDTO.builder()
                .reportID(UUID.randomUUID())
                .title("Different Title")
                .description("Different Description")
                .category("TICKET")
                .status("RESOLVED")
                .createdAt(LocalDateTime.now().plusDays(1))
                .createdBy("another-user")
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

        ReportListDTO dto = ReportListDTO.builder()
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