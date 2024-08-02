package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.CiderImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiderImageRepository extends JpaRepository<CiderImage, Long> {

  Optional<CiderImage> findFirstByName(String name);
}
