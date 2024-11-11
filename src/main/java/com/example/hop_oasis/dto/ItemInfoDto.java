package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private List<String> imageName;
    private double averageRating;
    private int ratingCount;
    private List<Long> specialOfferIds;
    private ItemType itemType;
    private List<ItemOptionsDto> options;


}
