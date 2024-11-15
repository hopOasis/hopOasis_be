package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Beer;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


public interface BeerRepository extends JpaRepository<Beer, Long>, JpaSpecificationExecutor<Beer> {

    @Query("select distinct b.beerName from Beer b where b.id in (:ids)")
    Set<String> findNames(Collection<Long> ids);

}
