package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class ProductBundleInfoDto {
    private Long id;
    private String name;
    private String description;
    private List<String> productImageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    @Builder.Default
    private ItemType itemType = ItemType.PRODUCT_BUNDLE;
    private List<ProductBundleOptionsDto> options = new ArrayList<>();
}
