package id.ac.ui.cs.advprog.eventsphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import id.ac.ui.cs.advprog.eventsphere.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.enums.TransactionType;

class TransactionTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSetSuccessStatus() {
        Transaction transaction = new Transaction(
                "txn-003",
                "user-123",
                TransactionType.TOPUP_BALANCE.getValue(),
                TransactionStatus.FAILED.getValue()
        );

        transaction.setStatus(TransactionStatus.SUCCESS.getValue());
        assertEquals(TransactionStatus.SUCCESS.getValue(), transaction.getStatus());
    }

    @Test
    void testSetFailedStatus() {
        Transaction transaction = new Transaction(
                "txn-003",
                "user-123",
                TransactionType.TOPUP_BALANCE.getValue(),
                TransactionStatus.SUCCESS.getValue()
        );

        transaction.setStatus(TransactionStatus.FAILED.getValue());
        assertEquals(TransactionStatus.FAILED.getValue(), transaction.getStatus());
    }

    @Test
    void testSetInvalidStatusThrowsException() {
        Transaction transaction = new Transaction(
                "txn-004",
                "user-123",
                TransactionType.TOPUP_BALANCE.getValue(),
                TransactionStatus.SUCCESS.getValue()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            transaction.setStatus("INVALID_STATUS");
        });
    }

    @Test
    void testCreateTransactionSuccessWithTopUp() {
        Transaction transaction = new Transaction(
                "txn-005",
                "user-123",
                TransactionType.TOPUP_BALANCE.getValue(),
                TransactionStatus.SUCCESS.getValue()
        );

        assertEquals("txn-005", transaction.getTransactionId());
        assertEquals("user-123", transaction.getUserId());
        assertEquals(TransactionType.TOPUP_BALANCE.getValue(), transaction.getType());
        assertEquals(TransactionStatus.SUCCESS.getValue(), transaction.getStatus());
    }

    @Test
    void testCreateTransactionSuccessWithTicketPurchase() {
        Transaction transaction = new Transaction(
                "txn-006",
                "user-789",
                TransactionType.TICKET_PURCHASE.getValue(),
                TransactionStatus.SUCCESS.getValue()
        );
        assertEquals("txn-006", transaction.getTransactionId());
        assertEquals("user-789", transaction.getUserId());
        assertEquals(TransactionType.TICKET_PURCHASE.getValue(), transaction.getType());
        assertEquals(TransactionStatus.SUCCESS.getValue(), transaction.getStatus());
    }

    @Test
    void testCreateTransactionWithInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                    "txn-001",
                    "user-123",
                    "INVALID_TYPE",
                    TransactionStatus.SUCCESS.getValue()
            );
        });
    }

    @Test
    void testCreateTransactionWithInvalidStatus() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                    "txn-002",
                    "user-123",
                    TransactionType.TOPUP_BALANCE.getValue(),
                    "Lodon"
            );
        });
    }
}
