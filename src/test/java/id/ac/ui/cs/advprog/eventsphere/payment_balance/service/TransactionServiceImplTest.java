package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    private TransactionService transactionService;
    private AccessStrategy mockStrategy;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl();
        mockStrategy = mock(AccessStrategy.class);
        transactionService.setStrategy(mockStrategy);
    }

    @Test
    void testCreateTransactionDelegatesToStrategy() {
        Transaction trx = mock(Transaction.class);
        transactionService.createTransaction(trx);
        verify(mockStrategy).createTransaction(trx);
    }

    @Test
    void testDeleteTransactionDelegatesToStrategy() {
        transactionService.deleteTransaction("xyz");
        verify(mockStrategy).deleteTransaction("xyz");
    }

    @Test
    void testViewAllTransactionsDelegatesToStrategy() {
        transactionService.viewAllTransactions();
        verify(mockStrategy).viewAllTransactions();
    }

    @Test
    void testFilterTransactionsDelegatesToStrategy() {
        transactionService.filterTransactions("FAILED");
        verify(mockStrategy).filterTransactions("FAILED");
    }
}
