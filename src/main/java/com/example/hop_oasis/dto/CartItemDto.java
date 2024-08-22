package com.example.hop_oasis.dto;

import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CartItemDto {
    private Long cartId;
    private Long itemId;
    private String itemTitle;
    private double pricePerItem;
    private int quantity;
    private BigDecimal totalCost;
}
