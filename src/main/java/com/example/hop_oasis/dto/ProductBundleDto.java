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
public final class ProductBundleDto {

  private Long id;
  private String name;
  private double price;
  private String description;
  private List<ProductBundleImageDto> imageDto;

}
