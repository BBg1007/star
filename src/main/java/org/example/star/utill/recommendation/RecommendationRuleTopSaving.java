package org.example.star.utill.recommendation;

import org.example.star.constants.ProductType;
import org.example.star.constants.TransactionType;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.repositories.TransactionsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RecommendationRuleTopSaving implements RecommendationRuleSet {
    private final TransactionsRepository transactionsRepository;

    public RecommendationRuleTopSaving(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public List<Optional<Recommendation>> checkUserForRecommendationsById(UUID id) {
        boolean debitUser = transactionsRepository.checkProductUsageByUserByUserId(id, ProductType.DEBIT.getType());
        boolean requiredDepositsDebitAmount = transactionsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.DEPOSIT.getType(),
                ProductType.DEBIT.getType()) > 50000;
        boolean requiredDepositsSavingAmount = transactionsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.DEPOSIT.getType(),
                ProductType.SAVING.getType()) > 50000;
        boolean positiveDebitDepositWithdraw = (transactionsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.DEPOSIT.getType(),
                ProductType.DEBIT.getType())> transactionsRepository.getTransactionAmountByUserByUserId(id,
                TransactionType.WITHDRAW.getType(),
                ProductType.DEBIT.getType()));

        if (debitUser && (requiredDepositsDebitAmount || requiredDepositsSavingAmount) && positiveDebitDepositWithdraw) {
            return List.of(Optional.of(Recommendation.getTopSavingRecommendation()));
        }
        return List.of(Optional.empty());
    }
}
