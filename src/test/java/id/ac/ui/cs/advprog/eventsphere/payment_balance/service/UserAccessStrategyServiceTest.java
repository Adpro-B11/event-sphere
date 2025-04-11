package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.TicketPurchaseTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.TopUpTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TopUpTransactionRepository;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TicketPurchaseTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserAccessStrategyServiceTest {

    private TopUpTransactionRepository topUpRepo;
    private TicketPurchaseTransactionRepository ticketRepo;
    private UserAccessStrategy strategy;

    @BeforeEach
    void setUp() {
        topUpRepo = mock(TopUpTransactionRepository.class);
        ticketRepo = mock(TicketPurchaseTransactionRepository.class);
        strategy = new UserAccessStrategy(topUpRepo, ticketRepo);
    }

    @Test
    void testCreateTopUpTransactionSavesCorrectly() {
        TopUpTransaction trx = mock(TopUpTransaction.class);
        strategy.createTransaction(trx);
        verify(trx).setValidateStatus();
        verify(topUpRepo).save(trx);
    }

    @Test
    void testCreateTicketTransactionSavesCorrectly() {
        TicketPurchaseTransaction trx = mock(TicketPurchaseTransaction.class);
        strategy.createTransaction(trx);
        verify(trx).setValidateStatus();
        verify(ticketRepo).save(trx);
    }

    @Test
    void testCreateUnsupportedTransactionThrowsException() {
        var unknown = mock(Transaction.class);
        assertThrows(IllegalArgumentException.class, () -> strategy.createTransaction(unknown));
    }

    @Test
    void testDeleteTransactionThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> strategy.deleteTransaction("123"));
    }

    @Test
    void testFilterTransactionsThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> strategy.filterTransactions("SUCCESS"));
    }

    @Test
    void testViewAllTransactionsThrowsException() {
        assertThrows(UnsupportedOperationException.class, strategy::viewAllTransactions);
    }
}
