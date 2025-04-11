package id.ac.ui.cs.advprog.eventsphere.model;

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

        if ("BANK_TRANSFER".equals(method)) {
            String bankName = paymentData.get("bankName");
            if (bankName == null || bankName.trim().isEmpty()) {
                throw new IllegalArgumentException("Bank name must not be null or empty for bank transfers.");
            }
        }
    }

    public void setValidateStatus(Map<String, String> paymentData) {
        String accountNumber = paymentData.get("accountNumber");

        if ("BANK_TRANSFER".equals(method)) {
            if (accountNumber.length() != 10 || !accountNumber.matches("\\d+")) {
                this.status = "REJECTED";
                return;
            }
        } else if ("CREDIT_CARD".equals(method)) {
            if (accountNumber.length() != 16 || !accountNumber.matches("\\d+")) {
                this.status = "REJECTED";
                return;
            }
        }

        this.status = "SUCCESS";
    }
}
