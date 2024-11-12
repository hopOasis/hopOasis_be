package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long>, JpaSpecificationExecutor<ProductBundle> {

}
