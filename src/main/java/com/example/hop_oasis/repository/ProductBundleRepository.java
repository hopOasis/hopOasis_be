package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long>, JpaSpecificationExecutor<ProductBundle> {

    @Query("select pb from ProductBundle pb where pb.name like %?1%")
    List<ProductBundle> getBundlesWithSimilarName(String name, Pageable page);
}
