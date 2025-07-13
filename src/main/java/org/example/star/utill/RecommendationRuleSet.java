package org.example.star.utill;

import org.example.star.model.recomendation.Recommendation;

import java.util.Optional;
import java.util.UUID;
@FunctionalInterface
public interface RecommendationRuleSet {
    Optional<Recommendation> checkUserForRecommendationsById(UUID id);
}
