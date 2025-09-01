package org.example.star.repositories;

import org.example.star.constants.Queries;
import org.example.star.model.dto.DynamicRecommendationDto;
import org.example.star.model.rule.Rule;
import org.example.star.utill.Rule.RuleHandler;
import org.example.star.utill.json.JsonConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RecommendationsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JsonConverter jsonConverter;

    public RecommendationsRepository(@Qualifier("recommendationsPostgresJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate, JsonConverter jsonConverter) {
        this.jdbcTemplate = jdbcTemplate;
        this.jsonConverter = jsonConverter;
    }

    public DynamicRecommendationDto addRecommendation(DynamicRecommendationDto dto) {
        String insertSql = """
                INSERT INTO recommendations
                (product_name,product_id, text, rules)
                VALUES (:name,:id,:text,:rules::jsonb)
                RETURNING id
                """;
        Map<String, Object> params = new HashMap<>();
        params.put("name", dto.getName());
        params.put("id", dto.getProductId());
        params.put("text", dto.getText());
        params.put("rules", jsonConverter.toJsonList(dto.getRules()));

        Long generatedId = jdbcTemplate.queryForObject(insertSql, params, Long.class);

        String selectSql = "SELECT * FROM recommendations WHERE id = :id";
        return jdbcTemplate.queryForObject(selectSql,
                Collections.singletonMap("id", generatedId),
                (rs, rowNum) ->
                        new DynamicRecommendationDto(
                                rs.getLong("id"),
                                rs.getString("product_name"),
                                rs.getObject("product_id", UUID.class),
                                rs.getString("text"),
                                jsonConverter.fromJsonList(rs.getString("rules"), Rule.class))
        );
    }

    public List<DynamicRecommendationDto> getAllRecommendations() {
        String selectSql = "SELECT * FROM recommendations";

        return jdbcTemplate.query(selectSql, (rs, rowNum) ->
                new DynamicRecommendationDto(
                        rs.getLong("id"),
                        rs.getString("product_name"),
                        rs.getObject("product_id", UUID.class),
                        rs.getString("text"),
                        jsonConverter.fromJsonList(rs.getString("rules"), Rule.class)));
    }

    public boolean deleteRecommendation(Long id) {
        String sql = "DELETE FROM recommendations WHERE id = :id";
        int affectedRows = jdbcTemplate.update(sql, Collections.singletonMap("id", id));
        return affectedRows > 0;
    }

    public Boolean checkUserForRecommendationById(UUID id, DynamicRecommendationDto dto){
        Map<String, Queries> queriesMap = Queries.getQueryTemplates();
      return dto.getRules().stream()
                .allMatch(rule ->{
                    String sqlQuery = queriesMap.get(rule.getQueryName()).getQuery();
                    Map<String,Object> params = RuleHandler.buildSqlParameters(rule);
                    params.put("userId",id);
                    return jdbcTemplate.queryForObject(sqlQuery,params, Boolean.class);
                });

    }
}
