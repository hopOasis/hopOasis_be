package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SnackOptions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnackOptionsRepository extends JpaRepository<SnackOptions, Long> {
    Optional<SnackOptions> findBySnackIdAndWeight(Long snackId, double weight);
}
