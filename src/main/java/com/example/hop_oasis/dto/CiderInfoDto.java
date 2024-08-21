package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CiderInfoDto {
    private Long id;
    private String ciderName;
    private double volumeLarge;
    private double volumeSmall;
    private double priceLarge;
    private double priceSmall;
    private String description;
    private List<String> ciderImageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    private ItemType itemType = ItemType.CIDER;

}

