package com.example.hop_oasis.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class BeerDto {
    private Long id;
    private String beerName;
    private String description;
    private String beerColor;
    private List<ImageDto> image;
    @Valid
    private List<BeerOptionsDto> options;
}
