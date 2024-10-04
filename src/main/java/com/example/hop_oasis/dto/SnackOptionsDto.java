package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SnackOptionsDto {
    private Long id;
    private Double weight;
    private int quantity;
    private double price;
}
