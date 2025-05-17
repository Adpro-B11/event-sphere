package id.ac.ui.cs.advprog.eventsphere.payment_balance.factory;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFactoryProducerTest {

    private Map<String, String> paymentData;
    private Map<String, String> ticketData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
        ticketData = new HashMap<>();
    }

    @Test
    void testGetFactoryForTopUpTransaction() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("accountNumber", "1234567890");

        TransactionFactory factory = TransactionFactoryProducer.getFactory(
                TransactionType.TOPUP_BALANCE.getValue(),
                "txn-001",
                "user-123",
                50000,
                PaymentMethod.BANK_TRANSFER.getValue(),
                paymentData
        );

        assertInstanceOf(TopUpTransactionFactory.class, factory);
    }

    @Test
    void testGetFactoryForTicketPurchaseTransaction() {
        ticketData.put("ConcertVIP", "3");

        TransactionFactory factory = TransactionFactoryProducer.getFactory(
                TransactionType.TICKET_PURCHASE.getValue(),
                "txn-207",
                "user-207",
                100000,
                PaymentMethod.IN_APP_BALANCE.getValue(),
                ticketData
        );

        assertInstanceOf(TicketPurchaseTransactionFactory.class, factory);
    }

    @Test
    void testGetFactoryForInvalidTransactionType() {
        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        "INVALID_TYPE",
                        "txn-002",
                        "user-456",
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                )
        );
    }
}
