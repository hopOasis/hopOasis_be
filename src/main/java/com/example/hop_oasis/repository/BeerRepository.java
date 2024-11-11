package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Beer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface BeerRepository extends JpaRepository<Beer, Long>, JpaSpecificationExecutor<Beer> {

    @Query(value = "select * from beer order by rand() limit ?1", nativeQuery = true)
    List<Beer> findRandomRecords(int N);

    @Query("select b from Beer b where b.beerColor = :#{#beer.beerColor} and b.id <> :#{#beer.id}")
    List<Beer> getOtherBeersWithTheSameColor(@Param("beer") Beer beer, Pageable pageable);
}
