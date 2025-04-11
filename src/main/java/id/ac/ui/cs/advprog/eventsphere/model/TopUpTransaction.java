package id.ac.ui.cs.advprog.eventsphere.model;

import java.util.Map;

public class TopUpTransaction {

    private String transactionId;
    private String userId;
    private String type;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public TopUpTransaction(String transactionId, String userId, String type, String method, Map<String, String> paymentData) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.method = method;

        validateInput(paymentData);
        this.paymentData = paymentData;
    }
    public void validateInput(Map<String, String> paymentData) {}
}