package id.ac.ui.cs.advprog.eventsphere.payment_balance.model;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class TicketPurchaseTransaction extends Transaction {

    private String method;
    private final Map<String, String> ticketData;

    public TicketPurchaseTransaction(String transactionId,
                                     String userId,
                                     String type,
                                     String method,
                                     double amount,
                                     Map<String, String> ticketData) {
        super(transactionId,
                userId,
                type,
                TransactionStatus.PENDING.name(),
                amount);

        if (!Objects.equals(type, TransactionType.TICKET_PURCHASE.name())) {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        if (!Objects.equals(method,PaymentMethod.IN_APP_BALANCE.name())) {
            throw new IllegalArgumentException("Invalid payment method: " + method);
        }

        validateTransaction(method, ticketData);

        this.method = method;
        this.ticketData = ticketData;
    }


    @Override
    protected void validateTransaction(String method, Map<String, String> ticketData) {
        if (ticketData == null || ticketData.isEmpty()) {
            throw new IllegalArgumentException("Ticket data must not be null or empty.");
        }

        for (Map.Entry<String, String> entry : ticketData.entrySet()) {
            int quantity = getQuantity(entry);

            if (quantity <= 0) {
                throw new IllegalArgumentException("Ticket amount must be greater than 0.");
            }
        }
    }

    private static int getQuantity(Map.Entry<String, String> entry) {
        String ticketType = entry.getKey();
        String amountStr = entry.getValue();

        if (ticketType == null || ticketType.trim().isEmpty()) {
            throw new IllegalArgumentException("Ticket type must not be null or empty.");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ticket amount must be a valid integer.");
        }
        return quantity;
    }
}
