package org.example.star.service;

import org.example.star.model.dto.DynamicRecommendationDto;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.RecommendationsRepository;
import org.example.star.utill.Rule.RuleHandler;
import org.example.star.utill.recommendation.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> recommendationRules;
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationService(List<RecommendationRuleSet> recommendationRules, RecommendationsRepository recommendationsRepository) {
        this.recommendationRules = recommendationRules;
        this.recommendationsRepository = recommendationsRepository;
    }
    public List<Recommendation> getRecommendationForUserById(UUID id){
        return recommendationRules.stream()
             .map(rule->rule.checkUserForRecommendationsById(id))
                .flatMap(Collection::stream)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public DynamicRecommendationDto addDynamicRule(DynamicRecommendationDto dto){
        boolean ruleIsValid = dto.getRules().stream().allMatch(RuleHandler::ruleISValid);
        if (ruleIsValid) {
            return recommendationsRepository.addRecommendation(dto);
        }
        throw new IllegalArgumentException("Неверный формат правила.");
    }

    public List<DynamicRecommendationDto> getAllRecommendations() {
        return recommendationsRepository.getAllRecommendations();
    }

    public boolean deleteRecommendation(Long id) {
        return recommendationsRepository.deleteRecommendation(id);
    }


}
