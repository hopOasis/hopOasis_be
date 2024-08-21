package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class SnackInfoDto {
    private Long id;
    private String snackName;
    private double weightLarge;
    private double weightSmall;
    private double priceLarge;
    private double priceSmall;
    private String description;
    private List<String> snackImageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    private ItemType itemType = ItemType.SNACK;
}
