package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.dto.ReactionDto;
import com.example.hop_oasis.dto.ReviewDto;
import com.example.hop_oasis.dto.ReviewInfoDto;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.service.data.ReviewReactionService;
import com.example.hop_oasis.service.data.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ReviewMapper reviewMapper;
    private final ReviewReactionService reviewReactionService;

    @PostMapping
    public ResponseEntity<ReviewInfoDto> create(@RequestBody ReviewDto reviewDto) {
        Review review = reviewService.createReview(reviewDto);
        ReviewInfoDto reviewInfoDto = reviewMapper.toReviewInfoDto(review);
        return ResponseEntity.ok().body(reviewInfoDto);
    }

    @PostMapping("/{id}/reaction")
    public ResponseEntity<String> reactToReview(@PathVariable("id") Long id, @RequestBody ReactionDto reactionDto,
                                                Authentication authentication) {
        reviewReactionService.addReaction(id, reactionDto.getReaction(), authentication);
        return ResponseEntity.ok().body(reactionDto.getReaction() + " added");

    }

    @GetMapping
    public ResponseEntity<List<ReviewInfoDto>> getAllReviews() {
        return ResponseEntity.ok().body(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewInfoDto> getReviewById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(reviewService.getReviewById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewInfoDto> deleteReviewById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(reviewService.deleteReview(id));
    }


}
