package org.example.star.controller;


import org.example.star.model.dto.dynamicRecommendation.DynamicRecommendationDto;
import org.example.star.model.dto.RecommendationDto;
import org.example.star.model.dto.dynamicRecommendation.GiveOutCounterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.example.star.model.recomendation.Recommendation;
import org.example.star.service.RecommendationService;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController("/recommendation")
public class RecommendationController {
    @Autowired(required = false)
    private BuildProperties buildProperties;

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
    @DeleteMapping("/deleteRule/{product_id}")
    public ResponseEntity<Void> deleteDynamicRecommendation(@PathVariable("product_id") Long id) {
        if (recommendationService.deleteRecommendation(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/rule/stats")
    public ResponseEntity<Map<String,List<GiveOutCounterDTO>>> getStats() {
        return ResponseEntity.ok(Map.of("stats",recommendationService.getStats()));
    }

    @PostMapping("/management/clear-caches")
    public void clearAllCaches() {
        recommendationService.clearAllCaches();
    }

    @GetMapping("/management/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        String name = buildProperties != null ? buildProperties.getName() : "star";
        String version = buildProperties != null ? buildProperties.getVersion() : "1.0-SNAPSHOT";

        return  ResponseEntity.ok(Map.of("name", name, "version", version));
    }
}
