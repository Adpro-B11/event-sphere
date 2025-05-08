package id.ac.ui.cs.advprog.eventsphere.report.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateReportDTOTest {

    private CreateReportDTO createReportDTO;
    private final String TITLE = "Pembayaran Error";
    private final String DESCRIPTION = "Pembayaran tidak dapat diproses setelah checkout";
    private final String CATEGORY = "PAYMENT";
    private final String CATEGORY_REFERENCE = "PAY-123456";
    private final String ATTACHMENT_PATH = "https://example.com/attachment";
    private final String CREATED_BY = "user123";

    @BeforeEach
    void setUp() {
        createReportDTO = new CreateReportDTO(
                                TITLE,
                                DESCRIPTION,
                                CATEGORY,
                                CATEGORY_REFERENCE,
                                ATTACHMENT_PATH,
                                CREATED_BY);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(TITLE, createReportDTO.getTitle());
        assertEquals(DESCRIPTION, createReportDTO.getDescription());
        assertEquals(CATEGORY, createReportDTO.getCategory());
        assertEquals(CATEGORY_REFERENCE, createReportDTO.getCategoryReference());
        assertEquals(ATTACHMENT_PATH, createReportDTO.getAttachmentPath());
        assertEquals(CREATED_BY, createReportDTO.getCreatedBy());
    }

    @Test
    void testSetters() {
        String newTitle = "Tiket Tidak Valid";
        String newDescription = "Tiket yang dibeli tidak bisa digunakan saat masuk acara";
        String newCategory = "TICKET";
        String newCategoryReference = "TIX-789012";
        String newAttachmentPath = "https://example.com/attachmentnew";
        String newCreatedBy = "user456";

        createReportDTO.setTitle(newTitle);
        createReportDTO.setDescription(newDescription);
        createReportDTO.setCategory(newCategory);
        createReportDTO.setCategoryReference(newCategoryReference);
        createReportDTO.setAttachmentPath(newAttachmentPath);
        createReportDTO.setCreatedBy(newCreatedBy);

        assertEquals(newTitle, createReportDTO.getTitle());
        assertEquals(newDescription, createReportDTO.getDescription());
        assertEquals(newCategory, createReportDTO.getCategory());
        assertEquals(newCategoryReference, createReportDTO.getCategoryReference());
        assertEquals(newAttachmentPath, createReportDTO.getAttachmentPath());
        assertEquals(newCreatedBy, createReportDTO.getCreatedBy());
    }

    @Test
    void testEquals() {
        CreateReportDTO sameDTO = new CreateReportDTO(
                                        TITLE,
                                        DESCRIPTION,
                                        CATEGORY,
                                        CATEGORY_REFERENCE,
                                        ATTACHMENT_PATH,
                                        CREATED_BY);

        assertEquals(createReportDTO, sameDTO);

        CreateReportDTO differentDTO = new CreateReportDTO(
                                        "Judul berbeda",
                                            DESCRIPTION,
                                            CATEGORY,
                                            CATEGORY_REFERENCE,
                                            ATTACHMENT_PATH,
                                            CREATED_BY);

        assertNotEquals(createReportDTO, differentDTO);
    }

    @Test
    void testHashCode() {
        CreateReportDTO sameDTO = new CreateReportDTO(
                TITLE,
                DESCRIPTION,
                CATEGORY,
                CATEGORY_REFERENCE,
                ATTACHMENT_PATH,
                CREATED_BY);

        assertEquals(createReportDTO.hashCode(), sameDTO.hashCode());
    }

    @Test
    void testToString() {
        String toString = createReportDTO.toString();

        assertTrue(toString.contains(TITLE));
        assertTrue(toString.contains(DESCRIPTION));
        assertTrue(toString.contains(CATEGORY));
        assertTrue(toString.contains(CATEGORY_REFERENCE));
        assertTrue(toString.contains(ATTACHMENT_PATH));
        assertTrue(toString.contains(CREATED_BY));
    }
}
