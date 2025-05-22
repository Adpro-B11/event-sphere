package id.ac.ui.cs.advprog.eventsphere.payment_balance.strategy;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.*;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserAccessStrategyServiceTest {
    private TransactionRepository repo;
    private AccessStrategy strategy;
    private final String USER = UUID.randomUUID().toString();
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setup() {
        repo = mock(TransactionRepository.class);
        strategy = new UserAccessStrategy(repo, USER);
    }

    @Test
    void findById_returnsOnlyIfOwner() {
        Transaction t = mock(Transaction.class);
        when(t.getUserId()).thenReturn(USER);
        when(repo.findById("tx1")).thenReturn(Optional.of(t));
        assertTrue(strategy.findById("tx1").isPresent());

        Transaction other = mock(Transaction.class);
        when(other.getUserId()).thenReturn("someoneElse");
        when(repo.findById("tx2")).thenReturn(Optional.of(other));
        assertTrue(strategy.findById("tx2").isEmpty());
    }

    @Test
    void viewAllTransactions_filtersByUser() {
        Transaction t1 = mock(Transaction.class), t2 = mock(Transaction.class), t3 = mock(Transaction.class);
        when(t1.getUserId()).thenReturn(USER);
        when(t2.getUserId()).thenReturn("X");
        when(t3.getUserId()).thenReturn(USER);
        when(repo.findByFilters(eq(USER), any(), any(), any(), any(), any()))
                .thenReturn(List.of(t1, t3));
        List<Transaction> own = strategy.viewAllTransactions();
        assertEquals(2, own.size());
        assertTrue(own.contains(t1) && own.contains(t3));
    }

    @Test
    void filterTransactions_delegatesToRepoWithCorrectArgs() {
        strategy.filterTransactions(USER, "SUCCESS", "TOPUP", "BANK", now.minusDays(1), now);
        verify(repo).findByFilters(USER, "SUCCESS", "TOPUP", "BANK", now.minusDays(1), now);
    }

    @Test
    void deleteTransaction_throws() {
        assertThrows(UnsupportedOperationException.class,
                () -> strategy.deleteTransaction("tx1"));
    }
}
