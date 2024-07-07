package com.example.hop_oasis.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartItemDto {
    private Long itemId;
    private String itemTitle;
    private double pricePerItem;
    private int quantity;
    private double totalCost;
}
