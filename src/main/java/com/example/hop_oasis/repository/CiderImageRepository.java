package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.CiderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CiderImageRepository extends JpaRepository<CiderImage, Long> {
    Optional<CiderImage> findFirstByName(String name);
}
