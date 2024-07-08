package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SpecialOfferBeerDto {
    private Long id;
    private List<BeerDto> specialOfferBeers;
}
