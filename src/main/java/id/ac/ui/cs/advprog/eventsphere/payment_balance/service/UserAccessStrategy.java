package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.factory.TransactionFactoryProducer;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TransactionRepository;

import java.util.List;
import java.util.Map;

public class UserAccessStrategy implements AccessStrategy {
    private final TransactionRepository transactionRepo;
    private final TransactionFactoryProducer factoryProducer;

    public UserAccessStrategy(TransactionRepository transactionRepo,
                              TransactionFactoryProducer factoryProducer) {
        this.transactionRepo = transactionRepo;
        this.factoryProducer = factoryProducer;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        throw new UnsupportedOperationException("User cannot create transactions directly");
    }

    public void createTransaction(String type, String transactionId, String userId,
                                  double amount, String method, Map<String, String> data) {
        Transaction trx = transactionRepo.createAndSave(type, transactionId, userId,
                amount, method, data);
        trx.validateTransaction();
    }

    @Override
    public void deleteTransaction(String transactionId) {
        throw new UnsupportedOperationException("User cannot delete transactions");
    }

    @Override
    public List<Transaction> viewAllTransactions() {
        throw new UnsupportedOperationException("User cannot view all transactions");
    }

    @Override
    public List<Transaction> filterTransactions(String status) {
        throw new UnsupportedOperationException("User cannot filter transactions");
    }
}