package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BeerRepository extends JpaRepository<Beer, Long> {
    @Query("select b from Beer b join b.beerOptions bo " +
            "group by b.id " +
            "order by max(bo.price) asc")
    Page<Beer> findByPriceAsc(String beerName, Pageable pageable);

    @Query("select b from Beer b join b.beerOptions bo " +
            "group by b.id " +
            "order by max(bo.price) DESC")
    Page<Beer> findByPriceDesc(String beerName, Pageable pageable);

    @Query("select b from Beer b where :beerName is null or b.beerName = :beerName")
    Page<Beer> findByBeerName(String beerName, Pageable pageable);

}
