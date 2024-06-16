package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ProductBundleDto;
import com.example.hop_oasis.dto.ProductBundleInfoDto;
import com.example.hop_oasis.model.ProductBundle;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductBundleService {
    void saveProductBundle(MultipartFile file, ProductBundleDto productBundle);
    ProductBundleInfoDto getProductBundleById(Long id);
    List<ProductBundleInfoDto> getAllProductBundle();
    void update(ProductBundleInfoDto productBundleInfoDto, Long id);
    void deleteProductBundle(Long id);
}
