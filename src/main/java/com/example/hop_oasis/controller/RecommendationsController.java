package com.example.hop_oasis.controller;

import com.example.hop_oasis.convertor.ProposedProductsMapper;
import com.example.hop_oasis.dto.ProposedProductsDto;
import com.example.hop_oasis.handler.ValidItemType;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.service.RecommendationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
@Tag(name = "Recommendations", description = "APIs for getting product's recommendation")
@RequiredArgsConstructor
public class RecommendationsController {

    private final RecommendationsService recommendationsService;
    private final ProposedProductsMapper mapper;

    @GetMapping("/carts")
    @Operation(summary = "Get recommendations based on what product items are already in the cart")
    public ResponseEntity<ProposedProductsDto> forCart(
            @Parameter(description = "Cart id", required = true)
            @RequestParam("cartId") Long cartId) {

        return ResponseEntity.ok(
                mapper.toDto(recommendationsService.getForCart(cartId)));
    }

    @GetMapping("/products")
    @Operation(summary = "Get recommendations based on product")
    public ResponseEntity<ProposedProductsDto> forProduct(
            @Parameter(description = "product id", required = true)
            @RequestParam("productId") Long productId,

            @Parameter(description = "Product type, for now it's one of BEER, CIDER, SNACK, PRODUCT_BUNDLE",
                    required = true)
            @RequestParam("itemType") @ValidItemType ItemType itemType) {

        return ResponseEntity.ok(
                mapper.toDto(recommendationsService.getForProduct(productId, itemType)));
    }
}
