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
public class RecommendationSimpleCredit implements RecommendationRuleSet {

    private final TransactionsRepository transactionsRepository;

    public RecommendationSimpleCredit(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public List<Optional<Recommendation>> checkUserForRecommendationsById(UUID id) {
    boolean creditProductUsage = transactionsRepository.checkProductUsageByUserByUserId(id, ProductType.CREDIT.getType());
    boolean positiveDebitDepositWithdrawDiff = (transactionsRepository.getTransactionAmountByUserByUserId(id, TransactionType.DEPOSIT.getType(), ProductType.DEBIT.getType())-
                                                transactionsRepository.getTransactionAmountByUserByUserId(id,TransactionType.WITHDRAW.getType(), ProductType.DEBIT.getType())>0);
    boolean requiredSumDebitWithdraw = transactionsRepository.getTransactionAmountByUserByUserId(id,TransactionType.WITHDRAW.getType(), ProductType.DEBIT.getType())>100000;

        if (!creditProductUsage && positiveDebitDepositWithdrawDiff && requiredSumDebitWithdraw) {
        return List.of(Optional.of(Recommendation.getSimpleCreditRecommendation()));
        }
        return List.of(Optional.empty());
    }
}
