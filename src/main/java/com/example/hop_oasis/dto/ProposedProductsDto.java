package com.example.hop_oasis.dto;

import java.util.List;

public record ProposedProductsDto(
        List<BeerInfoDto> beers,
        List<CiderInfoDto> ciders,
        List<SnackInfoDto> snacks,
        List<ProductBundleInfoDto> bundles
) {
}
