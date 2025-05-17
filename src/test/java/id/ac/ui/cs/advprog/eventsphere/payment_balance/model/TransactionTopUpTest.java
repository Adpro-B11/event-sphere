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

public class TransactionTopUpTest {

    private Map<String, String> paymentData;
    private String userId;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
        userId = UUID.randomUUID().toString();
    }

    @Test
    void testCreateTopUpTransactionPending() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("accountNumber", "1234567890");

        var transaction = TransactionFactoryProducer.getFactory(
                TransactionType.TOPUP_BALANCE.getValue(),
                "txn-001",
                userId,
                50000,
                PaymentMethod.BANK_TRANSFER.getValue(),
                paymentData
        ).createTransaction();

        assertEquals("txn-001", transaction.getTransactionId());
        assertEquals(userId, transaction.getUserId());
        assertEquals(TransactionType.TOPUP_BALANCE.getValue(), transaction.getType());
        assertEquals(PaymentMethod.BANK_TRANSFER.getValue(),
                ((TopUpTransaction) transaction).getMethod());
        assertEquals(TransactionStatus.PENDING.getValue(), transaction.getStatus());
    }

    @Test
    void testInvalidAccountNumberTooShort() {
        paymentData.put("bankName", "Bank XYZ");
        paymentData.put("accountNumber", "12345678");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-002",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testInvalidAccountNumberWithLetters() {
        paymentData.put("bankName", "Bank DEF");
        paymentData.put("accountNumber", "1234abc890");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-003",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testInvalidAccountNumberWithSpecialCharacters() {
        paymentData.put("bankName", "Bank HIJ");
        paymentData.put("accountNumber", "12345@7890");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-004",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testInvalidAccountNumberTooLong() {
        paymentData.put("bankName", "Bank KLM");
        paymentData.put("accountNumber", "1234567890123");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-005",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testNullBankNameThrowsException() {
        paymentData.put("bankName", null);
        paymentData.put("accountNumber", "1234567890");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-006",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testEmptyBankNameThrowsException() {
        paymentData.put("bankName", "");
        paymentData.put("accountNumber", "1234567890");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-007",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testWhitespaceBankNameThrowsException() {
        paymentData.put("bankName", "   ");
        paymentData.put("accountNumber", "1234567890");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-008",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testNullAccountNumberThrowsException() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("accountNumber", null);

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-009",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testEmptyAccountNumberThrowsException() {
        paymentData.put("bankName", "Bank DEF");
        paymentData.put("accountNumber", "");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-010",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testWhitespaceAccountNumberThrowsException() {
        paymentData.put("bankName", "Bank XYZ");
        paymentData.put("accountNumber", "          ");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-011",
                        userId,
                        50000,
                        PaymentMethod.BANK_TRANSFER.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testValidCreditCardTransactionPending() {
        paymentData.put("accountNumber", "1234567812345678");

        var transaction = TransactionFactoryProducer.getFactory(
                TransactionType.TOPUP_BALANCE.getValue(),
                "txn-100",
                userId,
                50000,
                PaymentMethod.CREDIT_CARD.getValue(),
                paymentData
        ).createTransaction();

        assertEquals(transaction.getUserId(), userId);
        assertEquals(TransactionStatus.PENDING.getValue(), transaction.getStatus());
    }

    @Test
    void testCreditCardTooShortIsRejected() {
        paymentData.put("accountNumber", "123456789012345");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-101",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testCreditCardTooLongIsRejected() {
        paymentData.put("accountNumber", "12345678901234567");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-102",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testCardNumberWithLettersIsRejected() {
        paymentData.put("accountNumber", "1234abcd5678efgh");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-103",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testCardNumberWithSpecialCharactersIsRejected() {
        paymentData.put("accountNumber", "1234-5678-9012-3456");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-104",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testEmptyCardNumberThrowsException() {
        paymentData.put("accountNumber", "");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-105",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testWhitespaceCardNumberThrowsException() {
        paymentData.put("accountNumber", "                ");

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-106",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }

    @Test
    void testNullCardNumberThrowsException() {
        paymentData.put("accountNumber", null);

        assertThrows(IllegalArgumentException.class, () ->
                TransactionFactoryProducer.getFactory(
                        TransactionType.TOPUP_BALANCE.getValue(),
                        "txn-107",
                        userId,
                        50000,
                        PaymentMethod.CREDIT_CARD.getValue(),
                        paymentData
                ).createTransaction()
        );
    }
}
