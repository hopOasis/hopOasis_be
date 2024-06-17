package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductBundleImageRepository extends JpaRepository<ProductBundleImage,Long> {
    Optional<ProductBundleImage> findByName(String name);
}
