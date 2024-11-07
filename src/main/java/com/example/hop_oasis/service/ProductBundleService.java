package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.model.ProductBundle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductBundleService {
    ProductBundle saveProductBundle(ProductBundleDto productBundle);

    ProductBundleInfoDto getProductBundleById(Long id);

    Page<ProductBundleInfoDto> getAllProductBundleWithFilter(String bundleName, Pageable pageable, String sortDirection);

    ProductBundleInfoDto addRatingAndReturnUpdatedProductBundleInfo(Long itemId, double ratingValue);

    ProductBundleInfoDto update(ProductBundleDto productBundleDto, Long id);

    ProductBundleInfoDto deleteProductBundle(Long id);
}
