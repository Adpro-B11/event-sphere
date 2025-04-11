package id.ac.ui.cs.advprog.eventsphere.enums;

import lombok.Getter;

@Getter
public enum PaymentType {
    BANK_TRANSFER("BANK_TRANSFER"),
    CREDIT_CARD("CREDIT_CARD");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    public static boolean contains(String param) {
        for (PaymentType method : PaymentType.values()) {
            if (method.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}
