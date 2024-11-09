package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CiderRepository extends JpaRepository<Cider, Long>, JpaSpecificationExecutor<Cider> {
    @Query(value = "select * from cider order by rand() limit ?1", nativeQuery = true)
    List<Cider> findRandomRecords(int N);
}
