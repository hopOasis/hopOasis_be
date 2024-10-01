package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SnackOptionsDto {
    private double weight;
    private int quantity;
    private double price;
}
