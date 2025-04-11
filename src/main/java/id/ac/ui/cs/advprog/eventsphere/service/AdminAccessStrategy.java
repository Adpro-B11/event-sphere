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
