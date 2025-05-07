package id.ac.ui.cs.advprog.eventsphere.report.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportDTOTest {

    private ReportDTO reportDTO;
    private final String TITLE = "Pembayaran Error";
    private final String DESCRIPTION = "Pembayaran tidak dapat diproses setelah checkout";
    private final String CATEGORY = "PAYMENT";
    private final String CATEGORY_REFERENCE = "PAY-123456";
    private final String ATTACHMENT_PATH = "https://example.com/attachment";
    private final String CREATED_BY = "user123";

    @BeforeEach
    void setUp() {
        reportDTO = new ReportDTO(
                                TITLE,
                                DESCRIPTION,
                                CATEGORY,
                                CATEGORY_REFERENCE,
                                ATTACHMENT_PATH,
                                CREATED_BY);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(TITLE, reportDTO.getTitle());
        assertEquals(DESCRIPTION, reportDTO.getDescription());
        assertEquals(CATEGORY, reportDTO.getCategory());
        assertEquals(CATEGORY_REFERENCE, reportDTO.getCategoryReference());
        assertEquals(ATTACHMENT_PATH, reportDTO.getAttachmentPath());
        assertEquals(CREATED_BY, reportDTO.getCreatedBy());
    }

    @Test
    void testSetters() {
        String newTitle = "Tiket Tidak Valid";
        String newDescription = "Tiket yang dibeli tidak bisa digunakan saat masuk acara";
        String newCategory = "TICKET";
        String newCategoryReference = "TIX-789012";
        String newAttachmentPath = "https://example.com/attachmentnew";
        String newCreatedBy = "user456";

        reportDTO.setTitle(newTitle);
        reportDTO.setDescription(newDescription);
        reportDTO.setCategory(newCategory);
        reportDTO.setCategoryReference(newCategoryReference);
        reportDTO.setAttachmentPath(newAttachmentPath);
        reportDTO.setCreatedBy(newCreatedBy);

        assertEquals(newTitle, reportDTO.getTitle());
        assertEquals(newDescription, reportDTO.getDescription());
        assertEquals(newCategory, reportDTO.getCategory());
        assertEquals(newCategoryReference, reportDTO.getCategoryReference());
        assertEquals(newAttachmentPath, reportDTO.getAttachmentPath());
        assertEquals(newCreatedBy, reportDTO.getCreatedBy());
    }

    @Test
    void testEquals() {
        ReportDTO sameDTO = new ReportDTO(
                                        TITLE,
                                        DESCRIPTION,
                                        CATEGORY,
                                        CATEGORY_REFERENCE,
                                        ATTACHMENT_PATH,
                                        CREATED_BY);

        assertEquals(reportDTO, sameDTO);

        ReportDTO differentDTO = new ReportDTO(
                                        "Judul berbeda",
                                            DESCRIPTION,
                                            CATEGORY,
                                            CATEGORY_REFERENCE,
                                            ATTACHMENT_PATH,
                                            CREATED_BY);

        assertNotEquals(reportDTO, differentDTO);
    }

    @Test
    void testHashCode() {
        ReportDTO sameDTO = new ReportDTO(
                TITLE,
                DESCRIPTION,
                CATEGORY,
                CATEGORY_REFERENCE,
                ATTACHMENT_PATH,
                CREATED_BY);

        assertEquals(reportDTO.hashCode(), sameDTO.hashCode());
    }

    @Test
    void testToString() {
        String toString = reportDTO.toString();

        assertTrue(toString.contains(TITLE));
        assertTrue(toString.contains(DESCRIPTION));
        assertTrue(toString.contains(CATEGORY));
        assertTrue(toString.contains(CATEGORY_REFERENCE));
        assertTrue(toString.contains(ATTACHMENT_PATH));
        assertTrue(toString.contains(CREATED_BY));
    }
}
