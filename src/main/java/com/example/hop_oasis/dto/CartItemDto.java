package com.example.hop_oasis.dto;

import java.math.BigDecimal;
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
public final class CartItemDto {

  private Long itemId;
  private String itemTitle;
  private double pricePerItem;
  private int quantity;
  private BigDecimal totalCost;
}
