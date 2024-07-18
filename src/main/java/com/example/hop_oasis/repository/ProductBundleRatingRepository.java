package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundleRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBundleRatingRepository extends JpaRepository<ProductBundleRating, Long> {
    List<ProductBundleRating> findByProductBundleId(Long id);

    int countByProductBundleId(Long id);
}
