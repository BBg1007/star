package org.example.star.controller;


import org.springframework.http.ResponseEntity;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;
@RestController("/recommendation")
public class RecommendationController {
private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<RecommendationDto> getRecommendation(@PathVariable("user_id") UUID id) {
       List<Recommendation> recList = recommendationService.getRecommendationForUserById(id);
       RecommendationDto Dto = new RecommendationDto(id,recList);
       return ResponseEntity.ok(Dto);
    }


    public record RecommendationDto(UUID user_id, List<Recommendation> recommendations) {}
}
