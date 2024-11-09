package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Snack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SnackRepository extends JpaRepository<Snack, Long>, JpaSpecificationExecutor<Snack> {

    @Query(value = "select * from snacks order by rand() limit ?1", nativeQuery = true)
    List<Snack> findRandomRecords(int N);
}
