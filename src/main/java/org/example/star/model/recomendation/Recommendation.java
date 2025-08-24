package org.example.star.model.recomendation;

import org.example.star.constants.RecommendationsData;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

public class Recommendation {
    private final String name;
    private final UUID id;
    private final String text;

    public Recommendation(String name, UUID id, String text) {
        this.name = name;
        this.id = id;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public static Recommendation getInvest500Recommendation() {
        return new Recommendation(RecommendationsData.INVEST500.getName(),
                                  RecommendationsData.INVEST500.getId(),
                                   RecommendationsData.INVEST500.getText());
    }

    public static Recommendation getTopSavingRecommendation() {
        return new Recommendation(RecommendationsData.TOP_SAVING.getName(),
                RecommendationsData.TOP_SAVING.getId(),
                RecommendationsData.TOP_SAVING.getText());
    }

    public static Recommendation getSimpleCreditRecommendation() {
        return new Recommendation(RecommendationsData.SIMPLE_CREDIT.getName(),
                                  RecommendationsData.SIMPLE_CREDIT.getId(),
                                  RecommendationsData.SIMPLE_CREDIT.getText());
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(name, that.name) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
