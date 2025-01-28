package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ProductBundle;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long>, JpaSpecificationExecutor<ProductBundle> {

    @Query("select distinct pb.name from ProductBundle pb where pb.id in (:ids)")
    Set<String> findNamesByIds(Collection<Long> ids);

}
