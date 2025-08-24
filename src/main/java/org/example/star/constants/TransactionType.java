package org.example.star.constants;

public enum TransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAW("WITHDRAW");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
