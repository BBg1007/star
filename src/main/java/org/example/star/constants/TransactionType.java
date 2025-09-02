package org.example.star.constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<String> getTransactionTypes(){
        return Arrays.stream(TransactionType.values())
                .map(TransactionType::getType)
                .collect(Collectors.toList());
    }
}
