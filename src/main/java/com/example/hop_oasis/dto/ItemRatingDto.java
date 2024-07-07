package com.example.hop_oasis.dto;
import com.example.hop_oasis.model.ItemType;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ItemRatingDto {
    private Long itemId;
    private ItemType itemType;
    private double averageRating;
    private int ratingCount;
}
