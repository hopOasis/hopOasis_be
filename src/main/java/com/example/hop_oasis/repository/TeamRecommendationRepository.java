package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.TeamRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRecommendationRepository extends JpaRepository<TeamRecommendation, Long> {
}
