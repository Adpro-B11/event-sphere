package id.ac.ui.cs.advprog.eventsphere.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class Transaction {

    private static final List<String> VALID_TYPES = Arrays.asList("TOPUP_BALANCE", "TICKET_PURCHASE");
    private static final List<String> VALID_STATUSES = Arrays.asList("SUCCESS", "FAILED");

    private String transactionId;
    private String userId;
    private String type;
    private String status;

    public Transaction(String transactionId, String userId, String type, String status) {
        validateType(type);
        validateStatus(status);

        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.status = status;
    }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = status;
    }

    private void validateType(String type) {
        if (!VALID_TYPES.contains(type)) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }

    private void validateStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid transaction status: " + status);
        }
    }
}
