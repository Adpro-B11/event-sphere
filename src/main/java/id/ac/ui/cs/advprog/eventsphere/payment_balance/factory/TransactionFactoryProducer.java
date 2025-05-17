package id.ac.ui.cs.advprog.eventsphere.payment_balance.factory;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;

import java.util.Map;

public class TransactionFactoryProducer {
    public static TransactionFactory getFactory(String type,
                                                String transactionId,
                                                String userId,
                                                double amount,
                                                String method,
                                                Map<String, String> data) {
        if (TransactionType.TOPUP_BALANCE.getValue().equals(type)) {
            return new TopUpTransactionFactory(
                    transactionId, userId, amount, method, data
            );
        } else if (TransactionType.TICKET_PURCHASE.getValue().equals(type)) {
            return new TicketPurchaseTransactionFactory(
                    transactionId, userId, amount, method, data
            );
        }
        throw new IllegalArgumentException("Unknown transaction type: " + type);
    }
}
