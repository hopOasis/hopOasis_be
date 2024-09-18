package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long> {

    Page<ProductBundle> findAll(Pageable pageable);
}
