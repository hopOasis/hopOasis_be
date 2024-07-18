package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SnackRating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SnackRatingRepository extends JpaRepository<SnackRating, Long> {
    List<SnackRating> findBySnackId(Long id);
    int countBySnackId(Long id);
}
