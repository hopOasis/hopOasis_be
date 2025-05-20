package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.model.ReviewReaction;
import com.example.hop_oasis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, Long> {
    Optional<ReviewReaction> findByUserAndReview(User user, Review review);
}
