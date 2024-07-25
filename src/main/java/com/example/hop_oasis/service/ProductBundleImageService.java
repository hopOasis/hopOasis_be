package com.example.hop_oasis.service;
import com.example.hop_oasis.dto.ProductBundleImageUrlDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProductBundleImageService {
    ProductBundleImageUrlDto getProductBundleImage(String name);
    ProductBundleImageUrlDto addProductBundleImage(Long id, MultipartFile file);
    void deleteProductBundleImage(String name);
}
