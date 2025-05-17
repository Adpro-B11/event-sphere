package id.ac.ui.cs.advprog.eventsphere.payment_balance.model;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import lombok.Getter;

import java.util.Map;
import java.time.LocalDateTime;

@Getter
public abstract class Transaction {
    private final String transactionId;
    private final String userId;
    private final String type;
    private String status;
    private final double amount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Transaction(String transactionId,
                          String userId,
                          String type,
                          double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }

        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void setStatus(String status) {
        if (!TransactionStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public abstract void validateTransaction(String method, Map<String, String> paymentData);
}
