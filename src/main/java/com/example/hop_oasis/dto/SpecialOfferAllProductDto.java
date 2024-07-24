package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class SpecialOfferAllProductDto {
    private Long id;
    private String name;
    private boolean active;
    private List<BeerInfoDto> specialOfferBeers;
    private List<CiderInfoDto> specialOfferCiders;
    private List<SnackInfoDto> specialOfferSnacks;
    private List<ProductBundleInfoDto> specialOfferProductBundles;
}
