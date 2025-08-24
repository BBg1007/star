package org.example.star.service;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.utill.RecommendationRuleSet;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> recommendationRules;

    public RecommendationService(List<RecommendationRuleSet> recommendationRules) {
        this.recommendationRules = recommendationRules;
    }
    public List<Recommendation> getRecommendationForUserById(UUID id){
        return recommendationRules.stream()
             .map(rule->rule.checkUserForRecommendationsById(id))
             .filter(Optional::isPresent)
             .map(Optional::get)
             .collect(Collectors.toList());
    }
}
