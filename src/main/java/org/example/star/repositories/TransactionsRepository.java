package org.example.star.repositories;

import org.example.star.constants.Queries;
import org.example.star.model.rule.Rule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransactionsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TransactionsRepository(@Qualifier("recommendationsH2JdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean checkProductUsageByUserByUserId(UUID id, String productType) {
        Map<String,Object> params = new HashMap<>();
        params.put("userId",id);
        params.put("productType",productType);
        Boolean using = jdbcTemplate.queryForObject(
                """
        SELECT EXISTS(SELECT 1
            FROM TRANSACTIONS t
            JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
            WHERE t.USER_ID = :userId
            AND p.TYPE = :productType)
        """,
                params,
                Boolean.class);

        return Boolean.TRUE.equals(using);
    }

    public Integer getTransactionAmountByUserByUserId(UUID id, String transactionType, String productType) {
        Map<String,Object> params = new HashMap<>();
        params.put("userId",id);
        params.put("productType",productType);
        params.put("transactionType",transactionType);
        return jdbcTemplate.queryForObject(
                """
    SELECT SUM(amount)
    FROM transactions t
    JOIN products p ON t.product_id = p.id
    WHERE p.type = :productType
    AND t.user_id = :userId
    AND t.type = :transactionType
    """,
                params,
                Integer.class);
    }

    public Boolean getDynamicRuleQuery(String sqlQuery, Map<String, Object> params) {
        return jdbcTemplate.queryForObject(sqlQuery,params, Boolean.class);
    }

}
