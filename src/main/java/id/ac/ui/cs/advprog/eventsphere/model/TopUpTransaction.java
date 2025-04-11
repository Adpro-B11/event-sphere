package id.ac.ui.cs.advprog.eventsphere.model;

import id.ac.ui.cs.advprog.eventsphere.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.enums.TransactionType;
import id.ac.ui.cs.advprog.eventsphere.enums.TransactionStatus;
import lombok.Getter;

import java.util.Map;

@Getter
public class TopUpTransaction {

    private final String transactionId;
    private final String userId;
    private final String type;
    private final String method;
    private String status;
    private final Map<String, String> paymentData;

    public TopUpTransaction(String transactionId, String userId, String type, String method, Map<String, String> paymentData) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.method = method;

        validateInput(paymentData);
        this.paymentData = paymentData;
    }

    private void validateInput(Map<String, String> paymentData) {
        String accountNumber = paymentData.get("accountNumber");

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number must not be null or empty.");
        }

        if (!TransactionType.contains(type)) {
            throw new IllegalArgumentException("Invalid transaction type.");
        }

        if (!PaymentMethod.contains(method)) {
            throw new IllegalArgumentException("Invalid payment method.");
        }

        if (method.equals(PaymentMethod.BANK_TRANSFER.name())) {
            String bankName = paymentData.get("bankName");
            if (bankName == null || bankName.trim().isEmpty()) {
                throw new IllegalArgumentException("Bank name must not be null or empty for bank transfers.");
            }
        }
    }

    public void setValidateStatus(Map<String, String> paymentData) {
        String accountNumber = paymentData.get("accountNumber");

        if (method.equals(PaymentMethod.BANK_TRANSFER.name())) {
            if (accountNumber.length() != 10 || !accountNumber.matches("\\d+")) {
                this.status = TransactionStatus.FAILED.name();
                return;
            }
        } else if (method.equals(PaymentMethod.CREDIT_CARD.name())) {
            if (accountNumber.length() != 16 || !accountNumber.matches("\\d+")) {
                this.status = TransactionStatus.FAILED.name();
                return;
            }
        }

        this.status = TransactionStatus.SUCCESS.name();
    }
}
