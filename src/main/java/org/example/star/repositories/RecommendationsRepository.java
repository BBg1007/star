package org.example.star.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RecommendationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean checkProductUsageByUserByUserId(UUID id, String productType) {
        Boolean using = jdbcTemplate.queryForObject(
                """
        SELECT EXISTS(SELECT 1
            FROM TRANSACTIONS t
            JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
            WHERE t.USER_ID = ?
            AND p.TYPE = ?)
        """,
                Boolean.class,
                id,productType);

        return Boolean.TRUE.equals(using);
    }

    public Integer getTransactionAmountByUserByUserId(UUID id, String transactionType, String productType) {
        return jdbcTemplate.queryForObject(
                """
    SELECT SUM(amount)
    FROM transactions t
    JOIN products p ON t.product_id = p.id
    WHERE p.type = ?
    AND t.user_id = ?
    AND t.type = ?
    """,
                Integer.class,
                productType, id, transactionType);
    }
}
