package com.example.hop_oasis.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
