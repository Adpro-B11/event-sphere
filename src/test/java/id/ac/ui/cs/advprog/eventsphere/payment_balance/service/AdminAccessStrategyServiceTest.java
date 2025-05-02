package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminAccessStrategyServiceTest {

    private TransactionRepository transactionRepo;
    private AdminAccessStrategy strategy;

    @BeforeEach
    void setUp() {
        transactionRepo = mock(TransactionRepository.class);
        strategy = new AdminAccessStrategy(transactionRepo);
    }

    @Test
    void testDeleteTransaction_CallsRepositoryDeleteOnce() {
        String transactionId = "trx-123";
        strategy.deleteTransaction(transactionId);
        verify(transactionRepo, times(1)).deleteById(transactionId);
    }

    @Test
    void testCreateTransaction_ThrowsUnsupportedOperationException() {
        Transaction mockTrx = mock(Transaction.class);
        assertThrows(
                UnsupportedOperationException.class,
                () -> strategy.createTransaction(mockTrx),
                "Admin should not be able to create transactions"
        );
    }

    @Test
    void testFilterTransactions_CallsFindByStatus() {
        String status = "SUCCESS";
        strategy.filterTransactions(status);
        verify(transactionRepo, times(1)).findByStatus(status);
    }

    @Test
    void testViewAllTransactions_CallsFindAll() {
        strategy.viewAllTransactions();
        verify(transactionRepo, times(1)).findAll();
    }

    @Test
    void testViewAllTransactions_ReturnsCombinedList() {
        Transaction trx1 = mock(Transaction.class);
        Transaction trx2 = mock(Transaction.class);
        when(transactionRepo.findAll()).thenReturn(List.of(trx1, trx2));

        List<Transaction> result = strategy.viewAllTransactions();

        assertEquals(2, result.size());
    }

    @Test
    void testFilterTransactions_ReturnsFilteredList() {
        String status = "FAILED";
        Transaction failedTrx = mock(Transaction.class);
        when(transactionRepo.findByStatus(status)).thenReturn(List.of(failedTrx));

        List<Transaction> result = strategy.filterTransactions(status);

        assertEquals(1, result.size());
        assertSame(failedTrx, result.get(0));
    }
}
