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
public class RecommendationRuleInvest500 implements RecommendationRuleSet {
    private final TransactionsRepository transactionsRepository;

    public RecommendationRuleInvest500(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Override
    public List<Optional<Recommendation>> checkUserForRecommendationsById(UUID id) {
        if (transactionsRepository.checkProductUsageByUserByUserId(id, ProductType.DEBIT.getType())&&
            !transactionsRepository.checkProductUsageByUserByUserId(id,ProductType.INVEST.getType())&&
            transactionsRepository.getTransactionAmountByUserByUserId(id,TransactionType.DEPOSIT.getType(), ProductType.SAVING.getType())>1000) {
            return List.of(Optional.of(Recommendation.getInvest500Recommendation()));
        }
        return List.of(Optional.empty());
    }
}
