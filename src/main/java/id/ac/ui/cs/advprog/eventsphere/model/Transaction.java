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

    public Transaction(String transactionId, String userId, String type, String status) {
        if (!TransactionType.contains(type)) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        if (!TransactionStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }

        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.status = status;
    }

    public void setStatus(String status) {
        if (!TransactionStatus.contains(status)) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
        this.status = status;
    }
}
