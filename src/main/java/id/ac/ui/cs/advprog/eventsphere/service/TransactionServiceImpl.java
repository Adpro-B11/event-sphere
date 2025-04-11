package id.ac.ui.cs.advprog.eventsphere.service;

import id.ac.ui.cs.advprog.eventsphere.model.Transaction;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private AccessStrategy strategy;

    @Override
    public void setStrategy(AccessStrategy strategy) {
    }

    @Override
    public void createTransaction(Transaction trx) {
    }

    @Override
    public void deleteTransaction(String transactionId) {
    }

    @Override
    public List<Transaction> viewAllTransactions() {
    }

    @Override
    public List<Transaction> filterTransactions(String status) {
    }
}
