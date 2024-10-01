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
public final class SnackInfoDto {
    private Long id;
    private String snackName;
    private String description;
    private List<String> snackImageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    @Builder.Default
    private ItemType itemType = ItemType.SNACK;
    private List<SnackOptionsDto> options = new ArrayList<>();
}
