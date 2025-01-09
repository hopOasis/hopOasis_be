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
public final class BeerInfoDto {
    private Long id;
    private String beerName;
    private String description;
    private String beerColor;
    private List<String> imageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    @Builder.Default
    private ItemType itemType = ItemType.BEER;
    private List<BeerOptionsDto> options = new ArrayList<>();
    private List<ReviewInfoDto> reviews;

}
