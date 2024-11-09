package com.example.hop_oasis.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationsDto {
    private List<BeerInfoDto> beers;
    private List<CiderInfoDto> ciders;
    private List<SnackInfoDto> snacks;
    private List<ProductBundleInfoDto> bundles;
}
