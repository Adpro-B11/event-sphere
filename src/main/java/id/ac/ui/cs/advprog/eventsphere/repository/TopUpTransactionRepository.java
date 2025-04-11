package id.ac.ui.cs.advprog.eventsphere.repository;

import id.ac.ui.cs.advprog.eventsphere.model.TopUpTransaction;

import java.util.*;

public class TopUpTransactionRepository {

    private final Map<String, TopUpTransaction> topUpTransactionStorage = new HashMap<>();

    public TopUpTransaction save(TopUpTransaction trx) {
        topUpTransactionStorage.put(trx.getTransactionId(), trx);
        return trx;
    }

    public TopUpTransaction findById(String id) {
        return topUpTransactionStorage.get(id);
    }

    public List<TopUpTransaction> findAll() {
        return new ArrayList<>(topUpTransactionStorage.values());
    }

    public List<TopUpTransaction> findByStatus(String status) {
        List<TopUpTransaction> filtered = new ArrayList<>();
        for (TopUpTransaction trx : topUpTransactionStorage.values()) {
            if (trx.getStatus().equalsIgnoreCase(status)) {
                filtered.add(trx);
            }
        }
        return filtered;
    }

    public void deleteById(String id) {
        topUpTransactionStorage.remove(id);
    }
}
