package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundleOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductBundleOptionsRepository extends JpaRepository<ProductBundleOptions, Long> {
    Optional<ProductBundleOptions> findByProductBundleId(Long productBundleId);
}
