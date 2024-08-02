package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SnackImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackImageRepository extends JpaRepository<SnackImage, Long> {

  Optional<SnackImage> findFirstByName(String name);
}
