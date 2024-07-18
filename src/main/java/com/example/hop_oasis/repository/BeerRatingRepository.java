package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.BeerRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeerRatingRepository extends JpaRepository<BeerRating, Long> {
    List<BeerRating> findByBeerId(Long id);

    int countByBeerId(Long id);
}
