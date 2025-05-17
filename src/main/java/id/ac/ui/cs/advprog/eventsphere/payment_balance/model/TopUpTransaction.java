package id.ac.ui.cs.advprog.eventsphere.payment_balance.model;

import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionStatus;
import id.ac.ui.cs.advprog.eventsphere.payment_balance.enums.TransactionType;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

@Getter
public class TopUpTransaction extends Transaction {

    private String method;
    private Map<String, String> paymentData;

    public TopUpTransaction(String transactionId,
                            String userId,
                            String type,
                            double amount,
                            String method,
                            Map<String, String> paymentData) {
        super(transactionId, userId, type, amount);

        if (!Objects.equals(type, TransactionType.TOPUP_BALANCE.getValue())) {
            throw new IllegalArgumentException("Invalid transaction type.");
        }

        if (!Objects.equals(method, PaymentMethod.IN_APP_BALANCE.getValue())) {
            throw new IllegalArgumentException("Invalid payment method.");
        }

        validateTransaction(method, paymentData);

        this.method = method;
        this.paymentData = paymentData;
    }

    @Override
    public void validateTransaction(String method, Map<String, String> paymentData) {
        String accountNumber = paymentData.get("accountNumber");

        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number must not be null or empty.");
        }

        if (method.equals(PaymentMethod.BANK_TRANSFER.name())) {
            String bankName = paymentData.get("bankName");
            if (bankName == null || bankName.trim().isEmpty()) {
                throw new IllegalArgumentException("Bank name must not be null or empty for bank transfers.");
            }
            if (accountNumber.length() != 10 || !accountNumber.matches("\\d+")) {
                throw new IllegalArgumentException("Bank account number format is incorrect.");
            }
        } else if (method.equals(PaymentMethod.CREDIT_CARD.name())) {
            if (accountNumber.length() != 16 || !accountNumber.matches("\\d+")) {
                throw new IllegalArgumentException("Credit Card number format is incorrect.");
            }
        }
    }

}
