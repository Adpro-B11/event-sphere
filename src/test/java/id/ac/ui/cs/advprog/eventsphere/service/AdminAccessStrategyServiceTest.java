package id.ac.ui.cs.advprog.eventsphere.service;

import id.ac.ui.cs.advprog.eventsphere.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.repository.TopUpTransactionRepository;
import id.ac.ui.cs.advprog.eventsphere.repository.TicketPurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminAccessStrategyServiceTest {

    private TopUpTransactionRepository topUpRepo;
    private TicketPurchaseTransactionRepository ticketRepo;
    private AdminAccessStrategy strategy;

    @BeforeEach
    void setUp() {
        topUpRepo = mock(TopUpTransactionRepository.class);
        ticketRepo = mock(TicketPurchaseTransactionRepository.class);
        strategy = new AdminAccessStrategy(topUpRepo, ticketRepo);
    }

    @Test
    void testDeleteTransactionCallsBothRepos() {
        strategy.deleteTransaction("123");
        verify(topUpRepo).deleteById("123");
        verify(ticketRepo).deleteById("123");
    }

    @Test
    void testCreateTransactionThrowsException() {
        Transaction mockTrx = mock(Transaction.class);
        assertThrows(UnsupportedOperationException.class, () -> strategy.createTransaction(mockTrx));
    }

    @Test
    void testFilterTransactionsCallsBothRepos() {
        strategy.filterTransactions("PENDING");
        verify(topUpRepo).findByStatus("PENDING");
        verify(ticketRepo).findByStatus("PENDING");
    }

    @Test
    void testViewAllTransactionsCallsBothRepos() {
        strategy.viewAllTransactions();
        verify(topUpRepo).findAll();
        verify(ticketRepo).findAll();
    }
}
