package id.ac.ui.cs.advprog.eventsphere.payment_balance.repository;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactoryProducer;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.TicketPurchaseTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.TopUpTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
    private final Map<String, Transaction> transactionStorage = new HashMap<>();

    public Transaction createAndSave(String type,
                                     String transactionId,
                                     String userId,
                                     double amount,
                                     String method,
                                     Map<String, String> data) {
        Transaction transaction = TransactionFactoryProducer.getFactory(
                type,
                transactionId,
                userId,
                amount,
                method,
                data
        ).createTransaction();
        return save(transaction);
    }

    public Transaction save(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction must not be null");
        Objects.requireNonNull(transaction.getTransactionId(), "Transaction ID must not be null");
        transactionStorage.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactionStorage.get(id));
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactionStorage.values());
    }

    // Flexible filter (all can be null)
    public List<Transaction> findByFilters(
            String userId,
            String status,
            String type,
            String method,
            LocalDateTime createdAfter,
            LocalDateTime createdBefore
    ) {
        return transactionStorage.values().stream()
                .filter(trx -> userId == null || userId.equals(trx.getUserId()))
                .filter(trx -> status == null || status.equalsIgnoreCase(trx.getStatus()))
                .filter(trx -> type == null || type.equalsIgnoreCase(trx.getType()))
                .filter(trx -> {
                    if (method == null) return true;
                    if (trx instanceof TopUpTransaction tut)
                        return method.equalsIgnoreCase(tut.getMethod());
                    if (trx instanceof TicketPurchaseTransaction tpt)
                        return method.equalsIgnoreCase(tpt.getMethod());
                    return false;
                })
                .filter(trx -> {
                    // Pastikan field createdAt ada di model Transaction!
                    if (createdAfter != null && trx.getCreatedAt().isBefore(createdAfter)) return false;
                    if (createdBefore != null && trx.getCreatedAt().isAfter(createdBefore)) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }

    public void deleteById(String id) {
        transactionStorage.remove(id);
    }
}
