package id.ac.ui.cs.advprog.eventsphere.model;
import java.util.Map;

public class TicketPurchaseTransaction extends Transaction {

    private final Map<String, String> ticketData;

    public TicketPurchaseTransaction(String transactionId, String userId, String type, double amount, Map<String, String> ticketData) {
        super(transactionId, userId, type, TransactionStatus.FAILED.name(), amount);
        validateTicketData(ticketData);

        this.ticketData = ticketData;
    }

    private void validateTicketData(Map<String, String> ticketData) {}
}
