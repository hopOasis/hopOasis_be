package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.BeerRating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRatingRepository extends JpaRepository<BeerRating, Long> {

  List<BeerRating> findByBeerId(Long id);

  int countByBeerId(Long id);
}
