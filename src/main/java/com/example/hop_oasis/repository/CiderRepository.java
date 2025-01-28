package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cider;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface CiderRepository extends JpaRepository<Cider, Long>, JpaSpecificationExecutor<Cider> {

    @Query("select distinct c.ciderName from Cider c where c.id in (:ids)")
    Set<String> findNamesByIds(Collection<Long> ids);

}
