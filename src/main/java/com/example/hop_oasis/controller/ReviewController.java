package com.example.hop_oasis.controller;

//import com.example.hop_oasis.convertor.ReviewInfoMapper;

import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.dto.ReviewDto;
import com.example.hop_oasis.dto.ReviewInfoDto;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.service.data.ReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class  ReviewController {
    private final ReviewServiceImpl reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewInfoDto> create(@RequestBody ReviewDto reviewDto) {
        Review review = reviewService.createReview(reviewDto);
        ReviewInfoDto reviewInfoDto = reviewMapper.toReviewInfoDto(review);
        return ResponseEntity.ok().body(reviewInfoDto);
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
