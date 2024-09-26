package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.BeerOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BeerOptionsRepository extends JpaRepository<BeerOptions, Long> {
   Optional<BeerOptions> findByBeerIdAndVolume(Long beerId, double volume);

}
