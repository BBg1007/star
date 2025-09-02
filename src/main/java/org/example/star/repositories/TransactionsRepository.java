package org.example.star.repositories;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class TransactionsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Cache<CacheKey, Boolean> productUsageCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    private final Cache<CacheKey, Integer> transactionAmountCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    private final Cache<CacheKey, Boolean> dynamicRuleCache = Caffeine.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    public TransactionsRepository(@Qualifier("recommendationsH2JdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean checkProductUsageByUserByUserId(UUID id, String productType) {
        CacheKey cacheKey = new CacheKey(id, productType);
        long startTime = System.nanoTime();
        boolean result = productUsageCache.get(cacheKey, key -> {
            System.out.println("Нет кеша с ключом " + key);
            long queryStartTime = System.nanoTime();
            Map<String, Object> params = new HashMap<>();
            params.put("userId", id);
            params.put("productType", productType);
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

            long queryDuration = System.nanoTime() - queryStartTime;
            System.out.println("На запрос потребовалось " + queryDuration + "ns");
            return Boolean.TRUE.equals(using);
        });
        long totalDuration = System.nanoTime() - startTime;
        System.out.println("Время выполнения метода " + totalDuration + "ns");
        return result;
    }

    public Integer getTransactionAmountByUserByUserId(UUID id, String transactionType, String productType) {
        CacheKey cacheKey = new CacheKey(id, transactionType, productType);

        return transactionAmountCache.get(cacheKey, key -> {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", id);
            params.put("productType", productType);
            params.put("transactionType", transactionType);
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
        });

    }

    public Boolean getDynamicRuleQuery(String sqlQuery, Map<String, Object> params) {
        UUID userId = (UUID) params.get("userId");
        CacheKey cacheKey = new CacheKey(userId, sqlQuery, params.toString());
        return dynamicRuleCache.get(cacheKey, key -> {
            return jdbcTemplate.queryForObject(sqlQuery, params, Boolean.class);
        });
    }

    private static class CacheKey {
        private final UUID userId;
        private final String param1;
        private final String param2;

        public CacheKey(UUID userId, String param1) {
            this.userId = userId;
            this.param1 = param1;
            this.param2 = null;
        }

        public CacheKey(UUID userId, String param1, String param2) {
            this.userId = userId;
            this.param1 = param1;
            this.param2 = param2;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey cacheKey = (CacheKey) o;
            return Objects.equals(userId, cacheKey.userId) && Objects.equals(param1, cacheKey.param1) && Objects.equals(param2, cacheKey.param2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, param1, param2);
        }
    }

}
