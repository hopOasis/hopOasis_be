package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BeerDto {

    private Long id;
    private String beerName;
    private double volumeLarge;
    private double volumeSmall;
    private double priceLarge;
    private double priceSmall;
    private String description;
    private String bearColor;
    private List<ImageDto> image;
}
