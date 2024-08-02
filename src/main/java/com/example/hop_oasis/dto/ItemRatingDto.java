package com.example.hop_oasis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
