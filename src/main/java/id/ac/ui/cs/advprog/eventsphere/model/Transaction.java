package id.ac.ui.cs.advprog.eventsphere.model;

public class Transaction {
    private String transactionId;
    private String userId;
    private String type;
    private String status;

    public Transaction(String transactionId, String userId, String type, String status) {

        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.status = status;
    }

}
