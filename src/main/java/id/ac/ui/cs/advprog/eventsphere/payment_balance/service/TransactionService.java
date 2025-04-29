package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.Transaction;
import java.util.List;

public interface TransactionService {
    void setStrategy(AccessStrategy strategy);
    void createTransaction(Transaction trx);
    void deleteTransaction(String transactionId);
    List<Transaction> viewAllTransactions();
    List<Transaction> filterTransactions(String status);
}
