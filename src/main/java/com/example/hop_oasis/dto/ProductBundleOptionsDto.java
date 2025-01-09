package com.example.hop_oasis.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductBundleOptionsDto {
    private Long id;
    @Min(0)
    private int quantity;
    private double price;
}
