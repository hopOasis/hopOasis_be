package com.example.hop_oasis.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CartDto {
    private List<CartItemDto> items;
    private BigDecimal priceForAll;
}
