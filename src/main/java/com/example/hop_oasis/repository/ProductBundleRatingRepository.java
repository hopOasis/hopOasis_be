package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundleRating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBundleRatingRepository extends JpaRepository<ProductBundleRating, Long> {

  List<ProductBundleRating> findByProductBundleId(Long id);

  int countByProductBundleId(Long id);
}
