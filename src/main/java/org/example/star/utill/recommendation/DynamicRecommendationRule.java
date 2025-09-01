package org.example.star.utill.recommendation;

import org.example.star.constants.Queries;
import org.example.star.model.dto.DynamicRecommendationDto;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.RecommendationsRepository;
import org.example.star.repositories.TransactionsRepository;
import org.example.star.utill.Rule.RuleHandler;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class DynamicRecommendationRule implements RecommendationRuleSet{
    private final TransactionsRepository transactionsRepository;
    private final RecommendationsRepository recommendationsRepository;

    public DynamicRecommendationRule(TransactionsRepository transactionsRepository, RecommendationsRepository recommendationsRepository) {
        this.transactionsRepository = transactionsRepository;
        this.recommendationsRepository = recommendationsRepository;
    }


    @Override
    public List<Optional<Recommendation>> checkUserForRecommendationsById(UUID id) {
        List<DynamicRecommendationDto> dynamicRecommendations = recommendationsRepository.getAllRecommendations();
        if (dynamicRecommendations.isEmpty()) {
          return Collections.emptyList();
        }
        List<Optional<Recommendation>> result = new ArrayList<>();
        dynamicRecommendations.stream()
                .filter(dto -> dto.getRules().stream()
                        .map(rule -> {
                            String sqlQuery = RuleHandler.getSqlQuery(rule);
                            Map<String,Object> params = RuleHandler.buildSqlParameters(rule);
                            params.put("userId",id);
                            return transactionsRepository.getDynamicRuleQuery(sqlQuery,params);
                        }).allMatch(Boolean.TRUE::equals))
                .forEach(dto -> {
                    result.add(Optional.of(new Recommendation(dto.getName(),dto.getProductId(), dto.getText())));
                });
        return result;
        }


    }

