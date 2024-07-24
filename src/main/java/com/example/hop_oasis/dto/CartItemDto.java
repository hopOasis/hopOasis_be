package com.example.hop_oasis.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CartItemDto {
    private String itemTitle;
    private double pricePerItem;
    private int quantity;
    private double totalCost;
}
