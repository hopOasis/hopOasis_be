package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.RecommendationRequestDto;
import com.example.hop_oasis.dto.RecommendationResponseDto;
import com.example.hop_oasis.service.data.TeamRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/team-recommendations")
public class TeamRecommendationController {
    private final TeamRecommendationService recommendationService;

    @PostMapping
    public ResponseEntity<RecommendationResponseDto> createRecommendation(@RequestBody RecommendationRequestDto recommendationRequestDto) {
        RecommendationResponseDto recommendationResponseDto = recommendationService.addRecommendation(recommendationRequestDto);
        return ResponseEntity.ok().body(recommendationResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecommendationResponseDto> updateRecommendation(
            @RequestBody RecommendationRequestDto recommendationRequestDto, @PathVariable Long id) {
        RecommendationResponseDto recommendationResponseDto = recommendationService.updateRecommendation(recommendationRequestDto, id);
        return ResponseEntity.ok().body(recommendationResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<RecommendationResponseDto>> getAllRecommendations() {
        return ResponseEntity.ok().body(recommendationService.getAllRecommendations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecommendationResponseDto> getRecommendationById(@PathVariable Long id) {
        return ResponseEntity.ok().body(recommendationService.getRecommendationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecommendationResponseDto> deleteRecommendationById(@PathVariable Long id) {
        RecommendationResponseDto dto = recommendationService.deleteRecommendationById(id);
        return ResponseEntity.ok().body(dto);
    }

}
