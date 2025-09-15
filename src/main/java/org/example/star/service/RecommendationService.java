package org.example.star.service;

import org.example.star.model.dto.dynamicRecommendation.DynamicRecommendationDto;
import org.example.star.model.dto.dynamicRecommendation.GiveOutCounterDTO;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.RecommendationsRepository;
import org.example.star.repositories.TransactionsRepository;
import org.example.star.utill.Rule.RuleHandler;
import org.example.star.utill.recommendation.RecommendationRuleSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {
    private final List<RecommendationRuleSet> recommendationRules;
    private final RecommendationsRepository recommendationsRepository;
    private final TransactionsRepository transactionsRepository;

    public RecommendationService(List<RecommendationRuleSet> recommendationRules, RecommendationsRepository recommendationsRepository, TransactionsRepository transactionsRepository) {
        this.recommendationRules = recommendationRules;
        this.recommendationsRepository = recommendationsRepository;
        this.transactionsRepository = transactionsRepository;
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

    public List<GiveOutCounterDTO>getStats() {
        return recommendationsRepository.getStats();
    }

    public void clearAllCaches() {
        transactionsRepository.clearAllCaches();
    }

    public boolean isUserOfTgBot(long chatId) {
        return recommendationsRepository.wasVisit(chatId);
    }

    public List<Recommendation> getRecommendationByUserName(String userName) {
        Set<UUID> userId = transactionsRepository.getUserIdByName(userName);
        if (userId.size() != 1) {
            return Collections.EMPTY_LIST;
        }
        UUID uuidUser = userId.stream().findFirst().get();

        return getRecommendationForUserById(uuidUser);
    }

    public void markAsVisitor(long chatId) {
        recommendationsRepository.markAsVisitor(chatId);
    }


}
