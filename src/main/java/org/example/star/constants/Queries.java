package org.example.star.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Queries {
     USER_OF("USER_OF",
             """
                     SELECT EXISTS(SELECT 1
                         FROM TRANSACTIONS t
                         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                         WHERE t.USER_ID = :userId
                         AND p.TYPE = :productType)
                     """,
              Map.of("productType",ProductType.getProductTypes()),
             List.of("productType")),
    ACTIVE_USER_OF("ACTIVE_USER_OF",
            """
                    SELECT EXISTS(
                        SELECT 1
                        FROM TRANSACTIONS t
                        JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                        WHERE t.USER_ID = :userId
                        AND p.TYPE = :productType
                        LIMIT 5)
                    """,
            Map.of("productType", ProductType.getProductTypes()),
            List.of("productType")),
    TRANSACTION_SUM_COMPARE("TRANSACTION_SUM_COMPARE",
            """
                   SELECT EXISTS(
                   SELECT 1
                      FROM TRANSACTION t
                      JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                      WHERE p.TYPE = :productType
                      AND t.USER_ID = :userId
                      AND t.TYPE = :transactionType
                      GROUP BY t.user_id
                      HAVING SUM (t.amount) :sign :number
                      )
                   """,
                    Map.of("productType",ProductType.getProductTypes(),
                            "transactionType",TransactionType.getTransactionTypes(),
                             "sign",List.of("!=","=","<=", ">=", "<",">")),
                             List.of("productType","transactionType","sign","number")),
    TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW("TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW",
            """
                    SELECT EXISTS(
                    SELECT 1
                    FROM(
                    SELECT
                      SUM(CASE WHEN t.type = "DEPOSIT" THEN t.amount ELSE 0 END) as deposit_sum,
                      SUM(CASE WHEN t.type = "WITHDRAW" THEN t.amount ELSE 0 END) as withdraw_sum,
                    FROM TRANSACTION t
                    JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
                    WHEN p.TYPE = :productType
                    AND t.USER_ID = :user_id
                    ) sums
                    WHERE (sums.deposit_sum :sign sums.withdraw_sum))
                    """,
            Map.of("productType",ProductType.getProductTypes(),
                    "sign",List.of("!=","=","<=", ">=", "<",">")),

                     List.of("productType","sign"));


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
