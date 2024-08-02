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
public final class CiderDto {

  private Long id;
  private String ciderName;
  private double volumeLarge;
  private double volumeSmall;
  private double priceLarge;
  private double priceSmall;
  private String description;
  private List<CiderImageDto> image;
}
