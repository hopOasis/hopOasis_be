package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ItemRatingDto {
    private Long itemId;
    private double averageRating;
    private int ratingCount;
}
