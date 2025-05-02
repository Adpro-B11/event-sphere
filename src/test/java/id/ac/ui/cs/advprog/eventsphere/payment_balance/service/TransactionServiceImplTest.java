package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;
    private AccessStrategy mockStrategy;
    private UserAccessStrategy userStrategy;
    private TransactionRepository transactionRepo;
    private TransactionFactoryProducer factoryProducer;

    @BeforeEach
    void setUp() {
        transactionRepo = mock(TransactionRepository.class);
        factoryProducer = mock(TransactionFactoryProducer.class);
        userStrategy = new UserAccessStrategy(transactionRepo, factoryProducer);

        transactionService = new TransactionServiceImpl();
    }

    @Test
    void testCreateTransactionWithParameters_DelegatesToUserStrategy() {
        transactionService.setStrategy(userStrategy);

        transactionService.createTransaction(
                "TOPUP_BALANCE",
                "txn-123",
                "user-456",
                500000,
                "BANK_TRANSFER",
                Map.of("accountNumber", "1234567890")
        );

        verify(transactionRepo).createAndSave(
                "TOPUP_BALANCE",
                "txn-123",
                "user-456",
                500000,
                "BANK_TRANSFER",
                Map.of("accountNumber", "1234567890")
        );
    }

    @Test
    void testCreateTransactionWithObject_ThrowsForUserStrategy() {
        transactionService.setStrategy(userStrategy);
        Transaction mockTrx = mock(Transaction.class);

        assertThrows(UnsupportedOperationException.class, () -> {
            transactionService.createTransaction(mockTrx);
        });
    }

    @Test
    void testCreateTransactionWithNonUserStrategy_ThrowsException() {
        AccessStrategy adminStrategy = mock(AccessStrategy.class);
        transactionService.setStrategy(adminStrategy);

        assertThrows(UnsupportedOperationException.class, () -> {
            transactionService.createTransaction(
                    "TICKET_PURCHASE",
                    "txn-456",
                    "user-789",
                    750000,
                    null,
                    Map.of("VIP", "2")
            );
        });
    }

    @Test
    void testDeleteTransaction_DelegatesToStrategy() {
        transactionService.setStrategy(mockStrategy);

        transactionService.deleteTransaction("txn-123");

        verify(mockStrategy).deleteTransaction("txn-123");
    }

    @Test
    void testViewAllTransactions_DelegatesToStrategy() {
        transactionService.setStrategy(mockStrategy);

        transactionService.viewAllTransactions();

        verify(mockStrategy).viewAllTransactions();
    }

    @Test
    void testFilterTransactions_DelegatesToStrategy() {
        transactionService.setStrategy(mockStrategy);

        transactionService.filterTransactions("FAILED");

        verify(mockStrategy).filterTransactions("FAILED");
    }

    @Test
    void testSetStrategy_ChangesBehavior() {
        transactionService.setStrategy(userStrategy);

        assertDoesNotThrow(() ->
                transactionService.createTransaction(
                        "TOPUP_BALANCE",
                        "txn-123",
                        "user-456",
                        500000,
                        "BANK_TRANSFER",
                        Map.of()
                )
        );

        transactionService.setStrategy(mockStrategy);

        transactionService.deleteTransaction("txn-123");
        verify(mockStrategy).deleteTransaction("txn-123");
    }
}
