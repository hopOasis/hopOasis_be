package com.example.hop_oasis.dto;

import jakarta.validation.constraints.Min;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SnackOptionsDto {
    private Long id;
    private Double weight;
    @Min(0)
    private int quantity;
    private double price;
}
