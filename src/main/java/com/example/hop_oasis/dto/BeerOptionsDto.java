package com.example.hop_oasis.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BeerOptionsDto {
    private Long id;
    private Double volume;
    @Min(0)
    private int quantity;
    private double price;
}
