package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    void setStrategy(AccessStrategy strategy);
    void createTransaction(String type, String transactionId, String userId,
                           double amount, String method, Map<String, String> data);
    void deleteTransaction(String transactionId);
    List<Transaction> viewAllTransactions();
    List<Transaction> filterTransactions(String status);
}
