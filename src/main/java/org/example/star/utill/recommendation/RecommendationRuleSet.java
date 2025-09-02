package org.example.star.utill.recommendation;

import org.example.star.model.recomendation.Recommendation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@FunctionalInterface
public interface RecommendationRuleSet {
    List<Optional<Recommendation>> checkUserForRecommendationsById(UUID id);
}
