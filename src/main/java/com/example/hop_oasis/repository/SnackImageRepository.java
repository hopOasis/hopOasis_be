package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SnackImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SnackImageRepository extends JpaRepository<SnackImage, Long> {
    Optional<SnackImage> findFirstByName(String name);
}
