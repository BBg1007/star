package org.example.star.utill;

import org.example.star.constants.ProductType;
import org.example.star.constants.TransactionType;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
@Component
public class RecommendationRuleInvest500 implements RecommendationRuleSet {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationRuleInvest500(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Optional<Recommendation> checkUserForRecommendationsById(UUID id) {
        if (recommendationsRepository.checkProductUsageByUserByUserId(id, ProductType.DEBIT.getType())&&
            !recommendationsRepository.checkProductUsageByUserByUserId(id,ProductType.INVEST.getType())&&
            recommendationsRepository.getTransactionAmountByUserByUserId(id,TransactionType.DEPOSIT.getType(), ProductType.SAVING.getType())>1000) {
            return Optional.of(Recommendation.getInvest500Recommendation());
        }
        return Optional.empty();
    }
}
