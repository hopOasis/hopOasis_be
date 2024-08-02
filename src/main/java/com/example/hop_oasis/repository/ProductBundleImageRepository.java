package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundleImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBundleImageRepository extends JpaRepository<ProductBundleImage, Long> {

  Optional<ProductBundleImage> findFirstByName(String name);
}
