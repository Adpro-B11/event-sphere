package id.ac.ui.cs.advprog.eventsphere.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTopUpTest {

    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentData = new HashMap<>();
    }

    @Test
    void testCreateTopUpTransactionSuccess() {
        paymentData.put("bankName", "Bank ABC");
        paymentData.put("accountNumber", "1234567890");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-001",
                "user-123",
                "TOPUP_BALANCE",
                "BANK_TRANSFER",
                paymentData
        );
        transaction.setValidateStatus(paymentData);

        assertEquals("txn-001", transaction.getTransactionId());
        assertEquals("user-123", transaction.getUserId());
        assertEquals("TOPUP_BALANCE", transaction.getType());
        assertEquals("SUCCESS", transaction.getStatus());
        assertEquals("Bank ABC", transaction.getBankName());
        assertEquals("1234567890", transaction.getAccountNumber());
    }

    @Test
    void testInvalidAccountNumberTooShortDoesNotThrowException() {
        paymentData.put("bankName", "Bank XYZ");
        paymentData.put("accountNumber", "12345678");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-002",
                "user-456",
                "TOPUP_BALANCE",
                "BANK_TRANSFER",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testInvalidAccountNumberWithLettersDoesNotThrowException() {
        paymentData.put("bankName", "Bank DEF");
        paymentData.put("accountNumber", "1234abc890");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-003",
                "user-789",
                "TOPUP_BALANCE",
                "BANK_TRANSFER",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testInvalidAccountNumberWithSpecialCharactersDoesNotThrowException() {
        paymentData.put("bankName", "Bank HIJ");
        paymentData.put("accountNumber", "12345@7890");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-004",
                "user-890",
                "TOPUP_BALANCE",
                "BANK_TRANSFER",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testInvalidAccountNumberTooLongDoesNotThrowException() {
        paymentData.put("bankName", "Bank KLM");
        paymentData.put("accountNumber", "1234567890123");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-005",
                "user-999",
                "TOPUP_BALANCE",
                "BANK_TRANSFER",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testNullBankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("bankName", null);
            paymentData.put("accountNumber", "1234567890");
            new TopUpTransaction("txn-006", "user-234", "TOPUP_BALANCE", "BANK_TRANSFER", paymentData);
        });
    }

    @Test
    void testEmptyBankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("bankName", "");
            paymentData.put("accountNumber", "1234567890");
            new TopUpTransaction("txn-007", "user-345", "TOPUP_BALANCE", "BANK_TRANSFER", paymentData);
        });
    }

    @Test
    void testWhitespaceBankNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("bankName", "   ");
            paymentData.put("accountNumber", "1234567890");
            new TopUpTransaction("txn-008", "user-456", "TOPUP_BALANCE", "BANK_TRANSFER", paymentData);
        });
    }

    @Test
    void testNullAccountNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("bankName", "Bank ABC");
            paymentData.put("accountNumber", null);
            new TopUpTransaction("txn-009", "user-567", "TOPUP_BALANCE", "BANK_TRANSFER", paymentData);
        });
    }

    @Test
    void testEmptyAccountNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("bankName", "Bank DEF");
            paymentData.put("accountNumber", "");
            new TopUpTransaction("txn-010", "user-678", "TOPUP_BALANCE", "BANK_TRANSFER", paymentData);
        });
    }

    @Test
    void testWhitespaceAccountNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("bankName", "Bank XYZ");
            paymentData.put("accountNumber", "          ");
            new TopUpTransaction("txn-011", "user-789", "TOPUP_BALANCE", "BANK_TRANSFER", paymentData);
        });
    }

    @Test
    void testValidCreditCardTransactionSuccess() {
        paymentData.put("accountNumber", "1234567812345678");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-100",
                "user-100",
                "TOPUP_BALANCE",
                "CREDIT_CARD",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("SUCCESS", transaction.getStatus());
    }

    @Test
    void testCardNumberTooShortIsRejected() {
        paymentData.put("accountNumber", "123456789012345");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-101",
                "user-101",
                "TOPUP_BALANCE",
                "CREDIT_CARD",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testCardNumberTooLongIsRejected() {
        paymentData.put("accountNumber", "12345678901234567");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-102",
                "user-102",
                "TOPUP_BALANCE",
                "CREDIT_CARD",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testCardNumberWithLettersIsRejected() {
        paymentData.put("accountNumber", "1234abcd5678efgh");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-103",
                "user-103",
                "TOPUP_BALANCE",
                "CREDIT_CARD",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testCardNumberWithSpecialCharactersIsRejected() {
        paymentData.put("accountNumber", "1234-5678-9012-3456");

        TopUpTransaction transaction = new TopUpTransaction(
                "txn-104",
                "user-104",
                "TOPUP_BALANCE",
                "CREDIT_CARD",
                paymentData
        );
        transaction.setValidateStatus(paymentData);
        assertEquals("REJECTED", transaction.getStatus());
    }

    @Test
    void testEmptyCardNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("accountNumber", "");
            new TopUpTransaction("txn-105", "user-105", "TOPUP_BALANCE", "CREDIT_CARD", paymentData);
        });
    }

    @Test
    void testWhitespaceCardNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("accountNumber", "                ");
            new TopUpTransaction("txn-106", "user-106", "TOPUP_BALANCE", "CREDIT_CARD", paymentData);
        });
    }

    @Test
    void testNullCardNumberThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentData.put("accountNumber", null);
            new TopUpTransaction("txn-107", "user-107", "TOPUP_BALANCE", "CREDIT_CARD", paymentData);
        });
    }

}
