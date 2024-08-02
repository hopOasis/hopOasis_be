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
public final class BeerInfoDto {

  private Long id;
  private String beerName;
  private double volumeLarge;
  private double volumeSmall;
  private double priceLarge;
  private double priceSmall;
  private String description;
  private String beerColor;
  private List<String> imageName;
  private double averageRating;
  private int ratingCount;
  private List<Long> specialOfferIds;
}
