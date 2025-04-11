package id.ac.ui.cs.advprog.eventsphere.repository;

import id.ac.ui.cs.advprog.eventsphere.model.TicketPurchaseTransaction;

import java.util.*;

public class TicketPurchaseTransactionRepository {
    private final Map<String, TicketPurchaseTransaction> transactions = new HashMap<>();

    public TicketPurchaseTransaction save(TicketPurchaseTransaction trx) {
        transactions.put(trx.getTransactionId(), trx);
        return trx;
    }

    public TicketPurchaseTransaction findById(String id) {
        return transactions.get(id);
    }

    public List<TicketPurchaseTransaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public List<TicketPurchaseTransaction> findByUserId(String userId) {
        return transactions.values().stream()
                .filter(trx -> trx.getUserId().equals(userId))
                .toList();
    }

    public List<TicketPurchaseTransaction> findByStatus(String status) {
        return transactions.values().stream()
                .filter(trx -> trx.getStatus().equals(status))
                .toList();
    }

}
