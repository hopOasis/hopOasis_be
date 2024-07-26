package com.example.hop_oasis.service;
import com.example.hop_oasis.dto.ImageUrlDto;

import com.example.hop_oasis.dto.ProductBundleInfoDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProductBundleImageService {
    ImageUrlDto getProductBundleImage(String name);
    ProductBundleInfoDto addProductBundleImage(Long id, MultipartFile file);
    void deleteProductBundleImage(String name);
}
