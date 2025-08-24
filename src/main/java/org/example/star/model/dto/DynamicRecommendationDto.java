package org.example.star.model.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.model.rule.Rule;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Entity
@Table(name = "recommendations")
public class DynamicRecommendationDto extends Recommendation {
    @NotNull
    private List<Rule> rules;
    private final Long recommendation_id;

    public DynamicRecommendationDto(Long recommendation_id, String name, UUID product_id, String text, List<Rule> rules) {
        super(name, product_id, text);
        this.recommendation_id = recommendation_id;
        this.rules = rules;
    }

    public UUID getProductId() {return super.getId();}

    public Long getRecommendation_id() {
        return recommendation_id;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public Optional<Recommendation> getRecommendation(){
        return Optional.of(new Recommendation(this.getName(),this.getProductId(),this.getText()));
    }
}
