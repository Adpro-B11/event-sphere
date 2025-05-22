package id.ac.ui.cs.advprog.eventsphere.payment_balance.strategy;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AccessStrategy {
    Optional<Transaction> findById(String transactionId);
    List<Transaction> viewAllTransactions();
    List<Transaction> viewUserTransactions(String userId);
    List<Transaction> filterTransactions(
            String userId,
            String status,
            String type,
            String method,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore
    );
    void deleteTransaction(String transactionId);
}