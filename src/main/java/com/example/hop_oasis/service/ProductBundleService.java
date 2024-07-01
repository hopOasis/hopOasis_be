package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.model.ProductBundle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductBundleService {
    void saveProductBundle(MultipartFile file, ProductBundleDto productBundle);
    ProductBundleInfoDto getProductBundleById(Long id);
    Page<ProductBundleInfoDto> getAllProductBundle(Pageable pageable);
    void update(ProductBundleInfoDto productBundleInfoDto, Long id);
    void deleteProductBundle(Long id);
}
