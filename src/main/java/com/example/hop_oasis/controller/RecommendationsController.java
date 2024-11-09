package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.RecommendationsMapper;
import com.example.hop_oasis.dto.RecommendationsDto;
import com.example.hop_oasis.handler.ValidItemType;
import com.example.hop_oasis.service.RecommendationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
public class RecommendationsController {

    private final RecommendationsService recommendationsService;
    private final RecommendationsMapper mapper;

    public RecommendationsController(RecommendationsService recommendationsService,
                                     RecommendationsMapper mapper) {
        this.recommendationsService = recommendationsService;
        this.mapper = mapper;
    }

    @GetMapping("/for-cart")
    public ResponseEntity<RecommendationsDto> forCart(@RequestParam("cartId") Long cartId) {
        return ResponseEntity.ok(
                mapper.toDto(recommendationsService.getForCart(cartId)));
    }

    @GetMapping("/for-product")
    public ResponseEntity<RecommendationsDto> forCart(@RequestParam("productId") Long productId,
                                                      @RequestParam("itemType") @ValidItemType String itemType) {
        return ResponseEntity.ok(
                mapper.toDto(recommendationsService.getForProduct(productId, itemType)));
    }
}
