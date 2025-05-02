package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactoryProducer;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.*;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAccessStrategyServiceTest {

    private TransactionRepository transactionRepo;
    private TransactionFactoryProducer factoryProducer;
    private UserAccessStrategy strategy;

    @BeforeEach
    void setUp() {
        transactionRepo = mock(TransactionRepository.class);
        factoryProducer = mock(TransactionFactoryProducer.class);
        strategy = new UserAccessStrategy(transactionRepo, factoryProducer);
    }

    @Test
    void testCreateTopUpTransaction_CallsFactoryAndSaves() {
        String type = "TOPUP_BALANCE";
        String transactionId = "txn-123";
        String userId = "user-456";
        double amount = 500000;
        String method = "BANK_TRANSFER";
        Map<String, String> data = Map.of("accountNumber", "1234567890", "bankName", "BCA");

        strategy.createTransaction(type, transactionId, userId, amount, method, data);

        verify(factoryProducer).getFactory(type, transactionId, userId, amount, method, data);
        verify(transactionRepo).createAndSave(type, transactionId, userId, amount, method, data);
    }

    @Test
    void testCreateTicketPurchaseTransaction_CallsFactoryAndSaves() {
        String type = "TICKET_PURCHASE";
        String transactionId = "txn-456";
        String userId = "user-789";
        double amount = 750000;
        Map<String, String> data = Map.of("VIP", "2", "Regular", "3");

        strategy.createTransaction(type, transactionId, userId, amount, null, data);

        verify(factoryProducer).getFactory(type, transactionId, userId, amount, null, data);
        verify(transactionRepo).createAndSave(type, transactionId, userId, amount, null, data);
    }

    @Test
    void testCreateTransaction_ValidatesStatusAutomatically() {
        String type = "TOPUP_BALANCE";
        Transaction mockTrx = mock(TopUpTransaction.class);
        when(transactionRepo.createAndSave(any(), any(), any(), anyDouble(), any(), any()))
                .thenReturn(mockTrx);

        strategy.createTransaction(type, "txn-123", "user-456", 500000, "BANK_TRANSFER", Map.of());

        verify(mockTrx).setValidateStatus();
    }

    @Test
    void testCreateUnsupportedTransactionType_ThrowsException() {
        String invalidType = "INVALID_TYPE";

        assertThrows(IllegalArgumentException.class, () -> {
            strategy.createTransaction(invalidType, "txn-123", "user-456", 500000, null, Map.of());
        });
    }

    @Test
    void testDeleteTransaction_ThrowsUnsupportedOperation() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> strategy.deleteTransaction("txn-123"),
                "User should not be able to delete transactions"
        );
    }

    @Test
    void testViewAllTransactions_ThrowsUnsupportedOperation() {
        assertThrows(
                UnsupportedOperationException.class,
                strategy::viewAllTransactions,
                "User should not be able to view all transactions"
        );
    }

    @Test
    void testFilterTransactions_ThrowsUnsupportedOperation() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> strategy.filterTransactions("SUCCESS"),
                "User should not be able to filter transactions"
        );
    }

    @Test
    void testCreateTransactionWithInvalidData_ThrowsException() {
        Map<String, String> invalidData = Map.of("accountNumber", "1234567890");

        assertThrows(IllegalArgumentException.class, () -> {
            strategy.createTransaction(
                    "TOPUP_BALANCE",
                    "txn-123",
                    "user-456",
                    500000,
                    "BANK_TRANSFER",
                    invalidData
            );
        });
    }
}
