package com.example.hop_oasis.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendationsDto {
    private List<BeerDto> beers;
    private List<CiderDto> ciders;
    private List<SnackDto> snacks;
    private List<ProductBundleDto> bundles;
}
