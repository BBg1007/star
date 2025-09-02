package org.example.star.constants;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ProductType {
    DEBIT("DEBIT"),
    CREDIT("CREDIT"),
    SAVING("SAVING"),
    INVEST("INVEST");

    private final String type;

    ProductType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static List<String> getProductTypes(){
        return Arrays.stream(ProductType.values())
                .map(ProductType::getType)
                .collect(Collectors.toList());
    }


}
