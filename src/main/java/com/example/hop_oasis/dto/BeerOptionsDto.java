package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BeerOptionsDto {
    private Long id;
    private Double volume;
    private int quantity;
    private double price;
}
