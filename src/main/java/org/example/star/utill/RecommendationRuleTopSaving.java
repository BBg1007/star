package org.example.star.utill;

import org.example.star.constants.ProductType;
import org.example.star.constants.TransactionType;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleTopSaving implements RecommendationRuleSet {
    private final RecommendationsRepository recommendationsRepository;

    public RecommendationRuleTopSaving(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Optional<Recommendation> checkUserForRecommendationsById(UUID id) {
        boolean debitUser = recommendationsRepository.checkProductUsageByUserByUserId(id, ProductType.DEBIT.getType());
        boolean requiredDepositsDebitAmount = recommendationsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.DEPOSIT.getType(),
                ProductType.DEBIT.getType()) > 50000;
        boolean requiredDepositsSavingAmount = recommendationsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.DEPOSIT.getType(),
                ProductType.SAVING.getType()) > 50000;
        boolean positiveDebitDepositWithdraw = (recommendationsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.DEPOSIT.getType(),
                ProductType.DEBIT.getType())>recommendationsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.WITHDRAW.getType(),
                ProductType.DEBIT.getType()));

        if (debitUser && (requiredDepositsDebitAmount || requiredDepositsSavingAmount) && positiveDebitDepositWithdraw) {
            return Optional.of(Recommendation.getTopSavingRecommendation());
        }
        return Optional.empty();
    }
}
