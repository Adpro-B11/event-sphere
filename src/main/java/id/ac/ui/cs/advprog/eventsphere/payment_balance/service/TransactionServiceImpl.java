package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import java.util.List;
import java.util.Map;

public class TransactionServiceImpl implements TransactionService {
    private AccessStrategy strategy;

    @Override
    public void setStrategy(AccessStrategy strategy) {
        this.strategy = strategy;
    }

    public void createTransaction(String type, String transactionId, String userId,
                                  double amount, String method, Map<String, String> data) {
        if (strategy instanceof UserAccessStrategy userStrategy) {
            userStrategy.createTransaction(type, transactionId, userId, amount, method, data);
        } else {
            throw new UnsupportedOperationException("Current strategy does not support this operation");
        }
    }

    @Override
    public void deleteTransaction(String transactionId) {
        strategy.deleteTransaction(transactionId);
    }

    @Override
    public List<Transaction> viewAllTransactions() {
        return strategy.viewAllTransactions();
    }

    @Override
    public List<Transaction> filterTransactions(String status) {
        return strategy.filterTransactions(status);
    }
}