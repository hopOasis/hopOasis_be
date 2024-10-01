package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.CiderOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CiderOptionsRepository extends JpaRepository<CiderOptions, Long> {
    Optional<CiderOptions> findByCiderIdAndVolume(Long ciderId, double volume);
}
