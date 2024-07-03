package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ProductBundleImageDto;
import com.example.hop_oasis.model.ProductBundleImage;
import org.springframework.web.multipart.MultipartFile;

public interface ProductBundleImageService {
    ProductBundleImageDto getProductBundleImage(String name);
    ProductBundleImage addProductBundleImage(Long id, MultipartFile file);
    void deleteProductBundleImage(String name);
}
