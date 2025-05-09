package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.Transaction;

import java.util.List;

public interface AccessStrategy {
    void createTransaction(Transaction transaction);
    void deleteTransaction(String transactionId);
    List<Transaction> viewAllTransactions();
    List<Transaction> filterTransactions(String status);
}
