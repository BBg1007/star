package org.example.star.model.dto;

import jakarta.validation.constraints.NotNull;
import org.example.star.model.recomendation.Recommendation;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class RecommendationDto {
    private final UUID userId;
    private final List<Recommendation> recommendations;

    public RecommendationDto(UUID userId, List<Recommendation> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }
}
