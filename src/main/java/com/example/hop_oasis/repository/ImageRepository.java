package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findFirstByName(String name);
}
