package org.example.star.model.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.model.rule.Rule;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Optional;
import java.util.UUID;




public class DynamicRecommendationDto extends Recommendation {
    @NotNull
    private List<Rule> rules;

    private Long recommendation_id;

    public DynamicRecommendationDto(){}

    public DynamicRecommendationDto(Long recommendation_id, String name,UUID product_id, String text,List<Rule> rules) {
        super(name, product_id, text);
        this.recommendation_id = recommendation_id;
        this.rules = rules;
    }
    @JsonProperty("product_id")
    public UUID getProductId() {return super.getId();}
    @JsonProperty("recommendation_id")
    public Long getRecommendation_id() {
        return recommendation_id;
    }
    @JsonProperty("rule")
    public List<Rule> getRules() {
        return rules;
    }
    @JsonProperty("rule")
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }


    @JsonProperty("product_name")
    public String getProductName() {
        return super.getName();
    }

    @JsonProperty("product_name")
    public void setProductName(String name) {
        super.setName(name);
    }

    @JsonProperty("product_text")
    public String getProductText() {
        return super.getText();
    }

    @JsonProperty("product_text")
    public void setProductText(String text) {
        super.setText(text);
    }
}
