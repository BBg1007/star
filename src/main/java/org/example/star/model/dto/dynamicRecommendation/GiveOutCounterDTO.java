package org.example.star.model.dto.dynamicRecommendation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GiveOutCounterDTO {
    private final Long ruleId;
    private final Long counter;

    public GiveOutCounterDTO(Long ruleId, Long counter) {
        this.ruleId = ruleId;
        this.counter = counter;
    }
    @JsonProperty("rule_id")
    public Long getRuleId() {
        return ruleId;
    }
    @JsonProperty("count")
    public Long getCounter() {
        return counter;
    }
}
