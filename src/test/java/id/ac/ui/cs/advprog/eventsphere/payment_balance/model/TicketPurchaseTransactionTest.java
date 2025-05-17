package id.ac.ui.cs.advprog.eventsphere.payment_balance.model;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactoryProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketPurchaseTransactionTest {

    private Map<String, String> ticketData;
    private String userId;

    @BeforeEach
    void setUp() {
        ticketData = new HashMap<>();
        userId = UUID.randomUUID().toString();
    }

    @Test
    void testEmptyTicketDataThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "txn-201",
                        userId,
                        100000,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                ).createTransaction()
        );
    }

    @Test
    void testTicketKeyIsNullThrowsException() {
        ticketData.put(null, "1");
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "txn-202",
                        userId,
                        100000,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                ).createTransaction()
        );
    }

    @Test
    void testTicketKeyIsEmptyStringThrowsException() {
        ticketData.put("", "1");
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "txn-203",
                        userId,
                        100000,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                ).createTransaction()
        );
    }

    @Test
    void testTicketKeyIsWhitespaceOnlyThrowsException() {
        ticketData.put("   ", "1");
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "txn-204",
                        userId,
                        100000,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                ).createTransaction()
        );
    }

    @Test
    void testNegativeTicketAmountThrowsException() {
        ticketData.put("ConcertVIP", "-3");
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "txn-205",
                        userId,
                        100000,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                ).createTransaction()
        );
    }

    @Test
    void testZeroTicketAmountThrowsException() {
        ticketData.put("ConcertVIP", "0");
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TICKET_PURCHASE.getValue(),
                        "txn-206",
                        userId,
                        100000,
                        PaymentMethod.IN_APP_BALANCE.getValue(),
                        ticketData
                ).createTransaction()
        );
    }

    @Test
    void testTicketPurchasePending() {
        ticketData.put("ConcertVIP", "3");
        ticketData.put("ConcertVVIP", "2");

        var transaction = TransactionFactoryProducer.getFactory(
                TransactionType.TICKET_PURCHASE.getValue(),
                "txn-207",
                userId,
                100000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        ).createTransaction();

        assertEquals("txn-207", transaction.getTransactionId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(TransactionType.TICKET_PURCHASE.getValue(), transaction.getType());
        assertEquals(PaymentMethod.IN_APP_BALANCE.getValue(),
                ((TicketPurchaseTransaction) transaction).getMethod());
        assertEquals(TransactionStatus.PENDING.getValue(), transaction.getStatus());
        assertEquals(100000, transaction.getAmount());

        Map<String, String> purchasedTickets = ((TicketPurchaseTransaction) transaction).getTicketData();
        assertEquals("3", purchasedTickets.get("ConcertVIP"));
        assertEquals("2", purchasedTickets.get("ConcertVVIP"));
    }

    @Test
    void testTicketPurchaseWithMultipleTypesPending() {
        ticketData.put("Regular", "5");
        ticketData.put("VIP", "2");
        ticketData.put("VVIP", "1");

        var transaction = TransactionFactoryProducer.getFactory(
                TransactionType.TICKET_PURCHASE.getValue(),
                "txn-208",
                userId,
                250000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        ).createTransaction();

        assertEquals("txn-208", transaction.getTransactionId());
        assertEquals(TransactionStatus.PENDING.getValue(), transaction.getStatus());

        Map<String, String> purchasedTickets = ((TicketPurchaseTransaction) transaction).getTicketData();
        assertEquals("5", purchasedTickets.get("Regular"));
        assertEquals("2", purchasedTickets.get("VIP"));
        assertEquals("1", purchasedTickets.get("VVIP"));
    }
}
