package org.example.star.controller;


import org.example.star.model.dto.DynamicRecommendationDto;
import org.example.star.model.dto.RecommendationDto;
import org.springframework.http.ResponseEntity;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.service.RecommendationService;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/rule")
    public ResponseEntity<DynamicRecommendationDto> addDynamicRule(@RequestBody DynamicRecommendationDto dto) {
        try {
            DynamicRecommendationDto result = recommendationService.addDynamicRule(dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(dto);
        }
    }
    @GetMapping("/rules")
    public ResponseEntity<List<DynamicRecommendationDto>> getAllRecommendations() {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }
    @DeleteMapping("/deleteRule/{user_id}")
    public ResponseEntity<Void> deleteDynamicRecommendation(@PathVariable("user_id") Long id) {
        if (recommendationService.deleteRecommendation(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
