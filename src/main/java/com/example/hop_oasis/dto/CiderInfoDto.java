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
public final class CiderInfoDto {
    private Long id;
    private String ciderName;
    private String description;
    private List<String> ciderImageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    @Builder.Default
    private ItemType itemType = ItemType.CIDER;
    private List<CiderOptionsDto> options = new ArrayList<>();

}

