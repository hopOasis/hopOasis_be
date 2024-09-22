package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BeerOptionsDto {
    private double volume;
    private int quantity;
    private double price;
}
