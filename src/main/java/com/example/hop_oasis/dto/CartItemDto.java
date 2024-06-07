package com.example.hop_oasis.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartItemDto {
    private String beerTitle;
    private double pricePerBeer;
    private int quantity;
    private BigDecimal value;
}
