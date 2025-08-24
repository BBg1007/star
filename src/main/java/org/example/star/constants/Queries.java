package org.example.star.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Queries {
     USER_OF("USER_OF",
             "SELECT CASE WHEN EXIST(SELECT 1 FROM TRANSACTIONS t " +
             "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID AND p.TYPE = productType " +
             "WHERE t.USER_ID = :userId)" +
             "THEN TRUE" +
             "ELSE FALSE" +
             "END",
              Map.of("productType",ProductType.getProductTypes()),
             List.of("productType"));

    private final String name;
    private final String query;
    private final Map<String, List<String>> possibleArgs;
    private final List<String> argOrder;

    private static final Map<String,Queries> queryTemplates;


    Queries(String name, String query, Map<String, List<String>> possibleArgs, List<String> argOrder) {
        this.name = name;
        this.query = query;
        this.possibleArgs = possibleArgs;
        this.argOrder = argOrder;
    }

    static {
        queryTemplates = new HashMap<>();
        for (Queries queries : values()) {
            Queries put = queryTemplates.put(queries.getName(), queries);
        }
    }

    public static Map<String,Queries> getQueryTemplates() {
        return queryTemplates;
    }

    public List<String> getArgsOrder(){
        return argOrder;
    }


    public Map<String, List<String>> getPossibleArgs() {
        return possibleArgs;
    }

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }

}
