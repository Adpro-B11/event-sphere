package id.ac.ui.cs.advprog.eventsphere.model;

import id.ac.ui.cs.advprog.eventsphere.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.enums.TransactionType;
import lombok.Getter;

@Getter
public class Transaction {
    private String transactionId;
    private String userId;
    private String type;
    private String status;
    private double amount;

    public Transaction(String transactionId, String userId, String type, String status, double amount) {
        if (!TransactionType.contains(type)) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        if (!TransactionStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }

        if (amount < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }

        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.amount = amount;
    }

    public void setStatus(String status) {
        if (!TransactionStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
        this.status = status;
    }
}
