package id.ac.ui.cs.advprog.eventsphere.report.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ReportMessageDTOTest {

    private ReportMessageDTO reportMessageDTO;
    private final UUID REPORT_ID = UUID.randomUUID();
    private final String SENDER = "admin123";
    private final String MESSAGE = "Kami sedang menyelidiki masalah pembayaran Anda";

    @BeforeEach
    void setUp() {
        reportMessageDTO = new ReportMessageDTO(REPORT_ID, SENDER, MESSAGE);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(REPORT_ID, reportMessageDTO.getReportID());
        assertEquals(SENDER, reportMessageDTO.getSender());
        assertEquals(MESSAGE, reportMessageDTO.getMessage());
    }

    @Test
    void testSetters() {
        UUID newReportId = UUID.randomUUID();
        String newSender = "support_team";
        String newMessage = "Masalah Anda sudah kami selesaikan";

        reportMessageDTO.setReportID(newReportId);
        reportMessageDTO.setSender(newSender);
        reportMessageDTO.setMessage(newMessage);

        assertEquals(newReportId, reportMessageDTO.getReportID());
        assertEquals(newSender, reportMessageDTO.getSender());
        assertEquals(newMessage, reportMessageDTO.getMessage());
    }

    @Test
    void testEquals() {
        ReportMessageDTO sameDTO = new ReportMessageDTO(REPORT_ID, SENDER, MESSAGE);

        assertEquals(reportMessageDTO, sameDTO);

        ReportMessageDTO differentDTO = new ReportMessageDTO(REPORT_ID, "sender_berbeda", MESSAGE);

        assertNotEquals(reportMessageDTO, differentDTO);
    }

    @Test
    void testHashCode() {
        ReportMessageDTO sameDTO = new ReportMessageDTO(REPORT_ID, SENDER, MESSAGE);

        assertEquals(reportMessageDTO.hashCode(), sameDTO.hashCode());
    }

    @Test
    void testToString() {
        String toString = reportMessageDTO.toString();

        assertTrue(toString.contains(REPORT_ID.toString()));
        assertTrue(toString.contains(SENDER));
        assertTrue(toString.contains(MESSAGE));
    }

    @Test
    void testWithDifferentReportId() {
        UUID anotherReportId = UUID.randomUUID();
        ReportMessageDTO anotherDTO = new ReportMessageDTO(anotherReportId, SENDER, MESSAGE);

        assertNotEquals(reportMessageDTO, anotherDTO);
    }
}
