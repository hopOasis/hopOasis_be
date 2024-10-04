package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductBundleOptionsDto {
    private Long id;
    private int quantity;
    private double price;
}
