package id.ac.ui.cs.advprog.eventsphere.report.dto;

import id.ac.ui.cs.advprog.eventsphere.report.dto.reportmessage.CreateReportMessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CreateReportMessageDTOTest {

    private CreateReportMessageDTO createReportMessageDTO;
    private final UUID REPORT_ID = UUID.randomUUID();
    private final String SENDER = "admin123";
    private final String MESSAGE = "Kami sedang menyelidiki masalah pembayaran Anda";

    @BeforeEach
    void setUp() {
        createReportMessageDTO = new CreateReportMessageDTO(REPORT_ID, SENDER, MESSAGE);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(REPORT_ID, createReportMessageDTO.getReportID());
        assertEquals(SENDER, createReportMessageDTO.getSender());
        assertEquals(MESSAGE, createReportMessageDTO.getMessage());
    }

    @Test
    void testSetters() {
        UUID newReportId = UUID.randomUUID();
        String newSender = "support_team";
        String newMessage = "Masalah Anda sudah kami selesaikan";

        createReportMessageDTO.setReportID(newReportId);
        createReportMessageDTO.setSender(newSender);
        createReportMessageDTO.setMessage(newMessage);

        assertEquals(newReportId, createReportMessageDTO.getReportID());
        assertEquals(newSender, createReportMessageDTO.getSender());
        assertEquals(newMessage, createReportMessageDTO.getMessage());
    }

    @Test
    void testEquals() {
        CreateReportMessageDTO sameDTO = new CreateReportMessageDTO(REPORT_ID, SENDER, MESSAGE);

        assertEquals(createReportMessageDTO, sameDTO);

        CreateReportMessageDTO differentDTO = new CreateReportMessageDTO(REPORT_ID, "sender_berbeda", MESSAGE);

        assertNotEquals(createReportMessageDTO, differentDTO);
    }

    @Test
    void testHashCode() {
        CreateReportMessageDTO sameDTO = new CreateReportMessageDTO(REPORT_ID, SENDER, MESSAGE);

        assertEquals(createReportMessageDTO.hashCode(), sameDTO.hashCode());
    }

    @Test
    void testToString() {
        String toString = createReportMessageDTO.toString();

        assertTrue(toString.contains(REPORT_ID.toString()));
        assertTrue(toString.contains(SENDER));
        assertTrue(toString.contains(MESSAGE));
    }

    @Test
    void testWithDifferentReportId() {
        UUID anotherReportId = UUID.randomUUID();
        CreateReportMessageDTO anotherDTO = new CreateReportMessageDTO(anotherReportId, SENDER, MESSAGE);

        assertNotEquals(createReportMessageDTO, anotherDTO);
    }
}
