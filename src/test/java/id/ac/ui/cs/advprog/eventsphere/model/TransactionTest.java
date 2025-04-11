package id.ac.ui.cs.advprog.eventsphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void testSetSuccessStatus() {
        Transaction transaction = new Transaction(
                "txn-003",
                "user-123",
                "TOPUP_BALANCE",
                "FAILED"
        );

        transaction.setStatus("SUCCESS");

        assertEquals("SUCCESS", transaction.getStatus());
    }

    @Test
    void testSetFailedStatus() {
        Transaction transaction = new Transaction(
                "txn-003",
                "user-123",
                "TOPUP_BALANCE",
                "FAILED"
        );

        transaction.setStatus("SUCCESS");
        assertEquals("SUCCESS", transaction.getStatus());
    }

    @Test
    void testSetInvalidStatusThrowsException() {
        Transaction transaction = new Transaction(
                "txn-004",
                "user-123",
                "TOPUP_BALANCE",
                "SUCCESS"
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
                "TOPUP_BALANCE",
                "SUCCESS"
        );

        assertEquals("txn-005", transaction.getTransactionId());
        assertEquals("user-123", transaction.getUserId());
        assertEquals("TOPUP_BALANCE", transaction.getType());
        assertEquals("SUCCESS", transaction.getStatus());
    }

    @Test
    void testCreateTransactionSuccessWithTicketPurchase() {
        Transaction transaction = new Transaction(
                "txn-006",
                "user-789",
                "TICKET_PURCHASE",
                "SUCCESS"
        );
        assertEquals("txn-006", transaction.getTransactionId());
        assertEquals("user-789", transaction.getUserId());
        assertEquals("TICKET_PURCHASE", transaction.getType());
        assertEquals("SUCCESS", transaction.getStatus());
    }

    @Test
    void testCreateTransactionWithInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                    "txn-001",
                    "user-123",
                    "INVALID_TYPE",
                    "SUCCESS"
            );
        });
    }

    @Test
    void testCreateTransactionWithInvalidStatus() {

        assertThrows(IllegalArgumentException.class, () -> {
            new Transaction(
                    "txn-002",
                    "user-123",
                    "TOPUP_BALANCE",
                    "LODON"
            );
        });
    }
}
