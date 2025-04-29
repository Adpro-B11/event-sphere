package id.ac.ui.cs.advprog.eventsphere.payment_balance.service;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.Transaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.TopUpTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.TicketPurchaseTransaction;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TopUpTransactionRepository;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.repository.TicketPurchaseTransactionRepository;

import java.util.List;

public class UserAccessStrategy implements AccessStrategy {

    private final TopUpTransactionRepository topUpRepo;
    private final TicketPurchaseTransactionRepository ticketRepo;

    public UserAccessStrategy(TopUpTransactionRepository topUpRepo, TicketPurchaseTransactionRepository ticketRepo) {
        this.topUpRepo = topUpRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        if (transaction instanceof TopUpTransaction topUp) {
            topUp.setValidateStatus();
            topUpRepo.save(topUp);
        } else if (transaction instanceof TicketPurchaseTransaction ticket) {
            ticket.setValidateStatus();
            ticketRepo.save(ticket);
        } else {
            throw new IllegalArgumentException("Unsupported transaction type");
        }
    }

    @Override
    public void deleteTransaction(String transactionId) {
        throw new UnsupportedOperationException("User cannot delete transactions");
    }

    @Override
    public List<Transaction> viewAllTransactions() {
        throw new UnsupportedOperationException("User cannot view all transactions");
    }

    @Override
    public List<Transaction> filterTransactions(String status) {
        throw new UnsupportedOperationException("User cannot filter transactions");
    }
}
