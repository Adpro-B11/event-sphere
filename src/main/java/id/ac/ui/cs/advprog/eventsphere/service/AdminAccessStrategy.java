package id.ac.ui.cs.advprog.eventsphere.service;

import id.ac.ui.cs.advprog.eventsphere.model.Transaction;
import id.ac.ui.cs.advprog.eventsphere.repository.TopUpTransactionRepository;
import id.ac.ui.cs.advprog.eventsphere.repository.TicketPurchaseTransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminAccessStrategy implements AccessStrategy {

    private final TopUpTransactionRepository topUpRepo;
    private final TicketPurchaseTransactionRepository ticketRepo;

    public AdminAccessStrategy(TopUpTransactionRepository topUpRepo, TicketPurchaseTransactionRepository ticketRepo) {
        this.topUpRepo = topUpRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        throw new UnsupportedOperationException("Admin cannot create transactions");
    }

    @Override
    public void deleteTransaction(String transactionId) {
        topUpRepo.deleteById(transactionId);
        ticketRepo.deleteById(transactionId);
    }

    @Override
    public List<Transaction> viewAllTransactions() {
        List<Transaction> all = new ArrayList<>();
        all.addAll(topUpRepo.findAll());
        all.addAll(ticketRepo.findAll());
        return all;
    }

    @Override
    public List<Transaction> filterTransactions(String status) {
        List<Transaction> filtered = new ArrayList<>();
        filtered.addAll(topUpRepo.findByStatus(status));
        filtered.addAll(ticketRepo.findByStatus(status));
        return filtered;
    }
}
