package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TransactionRepository;

import java.util.List;

public class AdminAccessStrategy implements AccessStrategy {
    private final TransactionRepository transactionRepo;

    public AdminAccessStrategy(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        throw new UnsupportedOperationException("Admin cannot create transactions");
    }

    public List<Transaction> viewUserTransactions(String userId){
        throw new UnsupportedOperationException("Admin cannot create transactions");
    }

    @Override
    public void deleteTransaction(String transactionId) {
        transactionRepo.deleteById(transactionId);
    }

    @Override
    public List<Transaction> viewAllTransactions() {
        return transactionRepo.findAll();
    }

    @Override
    public List<Transaction> filterTransactions(String status) {
        return transactionRepo.findByStatus(status);
    }
}