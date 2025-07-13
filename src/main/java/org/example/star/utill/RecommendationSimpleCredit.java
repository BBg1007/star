package org.example.star.utill;

import org.example.star.constants.ProductType;
import org.example.star.constants.TransactionType;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
@Component
public class RecommendationSimpleCredit implements RecommendationRuleSet{

    private final RecommendationsRepository recommendationsRepository;

    public RecommendationSimpleCredit(RecommendationsRepository recommendationsRepository) {
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public Optional<Recommendation> checkUserForRecommendationsById(UUID id) {
    boolean creditProductUsage = recommendationsRepository.checkProductUsageByUserByUserId(id, ProductType.CREDIT.getType());
    boolean positiveDebitDepositWithdrawDiff = (recommendationsRepository.getTransactionAmountByUserByUserId(id, TransactionType.DEPOSIT.getType(), ProductType.DEBIT.getType())-
                                                recommendationsRepository.getTransactionAmountByUserByUserId(id,TransactionType.WITHDRAW.getType(), ProductType.DEBIT.getType())>0);
    boolean requiredSumDebitWithdraw = recommendationsRepository.getTransactionAmountByUserByUserId(id,TransactionType.WITHDRAW.getType(), ProductType.DEBIT.getType())>100000;

        if (!creditProductUsage && positiveDebitDepositWithdrawDiff && requiredSumDebitWithdraw) {
        return Optional.of(Recommendation.getSimpleCreditRecommendation());
        }
        return Optional.empty();
    }
}
