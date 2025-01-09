package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ItemOptionsDto {
    private Long id;
    private Double measureValue;
    private int quantity;
    private double price;
}
