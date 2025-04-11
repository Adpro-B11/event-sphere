package id.ac.ui.cs.advprog.eventsphere.model;

import id.ac.ui.cs.advprog.eventsphere.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class TicketPurchaseTransactionTest {

    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }

    @Test
    void testEmptyTicketDataThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TicketPurchaseTransaction(
                    "txn-201",
                    "user-201",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
            );
        });
    }

    @Test
    void testTicketKeyIsNullThrowsException() {
        paymentData.put(null, "1");

        assertThrows(IllegalArgumentException.class, () -> {
            new TicketPurchaseTransaction(
                    "txn-202",
                    "user-202",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
            );
        });
    }

    @Test
    void testTicketKeyIsEmptyStringThrowsException() {
        paymentData.put("", "1");

        assertThrows(IllegalArgumentException.class, () -> {
            new TicketPurchaseTransaction(
                    "txn-203",
                    "user-203",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
            );
        });
    }

    @Test
    void testTicketKeyIsWhitespaceOnlyThrowsException() {
        paymentData.put("   ", "1");

        assertThrows(IllegalArgumentException.class, () -> {
            new TicketPurchaseTransaction(
                    "txn-204",
                    "user-204",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
            );
        });
    }

    @Test
    void testNegativeTicketAmountThrowsException() {
        paymentData.put("ConcertVIP", "-3");

        assertThrows(IllegalArgumentException.class, () -> {
            new TicketPurchaseTransaction(
                    "txn-205",
                    "user-205",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
            );
        });
    }

    @Test
    void testZeroTicketAmountThrowsException() {
        paymentData.put("ConcertVIP", "0");

        assertThrows(IllegalArgumentException.class, () -> {
            new TicketPurchaseTransaction(
                    "txn-206",
                    "user-206",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
            );
        });
    }

    @Test
    void testTicketPurchaseSuccess() {
        paymentData.put("ConcertVIP", "3");
        paymentData.put("ConcertVVIP", "2");

        TicketPurchaseTransaction transaction = new TicketPurchaseTransaction(
                    "txn-206",
                    "user-206",
                    "TICKET_PURCHASE",
                    100000,
                    paymentData
        );

        transaction.setValidateStatus();
        assertEquals("txn-206", transaction.getTransactionId());
        assertEquals("user-206", transaction.getUserId());
        assertEquals(TransactionType.TICKET_PURCHASE.getValue(), transaction.getType());
        assertEquals(TransactionStatus.SUCCESS.getValue(), transaction.getStatus());
        assertEquals(100000, transaction.getAmount());

    }
}
