package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.CiderRating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CiderRatingRepository extends JpaRepository<CiderRating, Long> {

  List<CiderRating> findByCiderId(Long id);

  int countByCiderId(Long id);
}
