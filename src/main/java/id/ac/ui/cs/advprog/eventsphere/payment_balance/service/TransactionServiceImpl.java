package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.Transaction;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private AccessStrategy strategy;

    @Override
    public void setStrategy(AccessStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void createTransaction(Transaction trx) {
        strategy.createTransaction(trx);
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
