package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecommendationRequestDto {
    private Long itemId;
    private ItemType itemType;
    private String authorName;
    private String teamRole;
    private String textRecommendation;
}
