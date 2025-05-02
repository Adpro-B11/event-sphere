package id.ac.ui.cs.advprog.eventsphere.payment_balance.repository;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactory;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactoryProducer;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.TicketPurchaseTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.TopUpTransaction;

import java.util.*;

public class TransactionRepository {
    private final Map<String, Transaction> transactionStorage = new HashMap<>();
    private final TransactionFactoryProducer factoryProducer;

    public TransactionRepository(TransactionFactoryProducer factoryProducer) {
        this.factoryProducer = Objects.requireNonNull(factoryProducer);
    }

    public Transaction createAndSave(String type, String transactionId, String userId,
                                     double amount, String method, Map<String, String> data) {
        TransactionFactory factory = factoryProducer.getFactory(type, transactionId, userId, amount, method, data);
        Transaction transaction = factory.createTransaction();
        return save(transaction);
    }

    public Transaction save(Transaction transaction) {
        Objects.requireNonNull(transaction);
        Objects.requireNonNull(transaction.getTransactionId());
        transaction.validateTransaction();
        transactionStorage.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactionStorage.get(id));
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactionStorage.values());
    }

    public List<Transaction> findByUserId(String userId) {
        Objects.requireNonNull(userId);
        return transactionStorage.values().stream()
                .filter(trx -> userId.equals(trx.getUserId()))
                .toList();
    }

    public List<Transaction> findByStatus(String status) {
        Objects.requireNonNull(status);
        return transactionStorage.values().stream()
                .filter(trx -> status.equalsIgnoreCase(trx.getStatus()))
                .toList();
    }

    public List<Transaction> findByType(String type) {
        Objects.requireNonNull(type);
        return transactionStorage.values().stream()
                .filter(trx -> type.equalsIgnoreCase(trx.getType()))
                .toList();
    }

    public List<TicketPurchaseTransaction> findAllTicketPurchases() {
        return transactionStorage.values().stream()
                .filter(trx -> trx instanceof TicketPurchaseTransaction)
                .map(trx -> (TicketPurchaseTransaction) trx)
                .toList();
    }

    public List<TopUpTransaction> findAllTopUps() {
        return transactionStorage.values().stream()
                .filter(trx -> trx instanceof TopUpTransaction)
                .map(trx -> (TopUpTransaction) trx)
                .toList();
    }

    public void deleteById(String id) {
        transactionStorage.remove(id);
    }
}