package id.ac.ui.cs.advprog.eventsphere.service;

import id.ac.ui.cs.advprog.eventsphere.model.Transaction;

import java.util.List;

public interface AccessStrategy {
    void createTransaction(Transaction transaction);
    void deleteTransaction(String transactionId);
    List<Transaction> viewAllTransactions();
    List<Transaction> filterTransactions(String status);
}
