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
public final class SnackDto {

  private Long id;
  private String snackName;
  private double weightLarge;
  private double weightSmall;
  private double priceLarge;
  private double priceSmall;
  private String description;
  private List<SnackImageDto> snackImageDto;

}
