package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.enums.Reaction;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.model.ReviewReaction;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.ReviewReactionRepository;
import com.example.hop_oasis.repository.ReviewRepository;
import com.example.hop_oasis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewReactionService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reactionRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public void addReaction(Long reviewId, Reaction reaction, Authentication authentication) {
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(() -> new IllegalArgumentException("Review not found with id"));
        Optional<ReviewReaction> optionalReviewReaction = reactionRepository.findByUserAndReview(user, review);
        if (optionalReviewReaction.isPresent()) {
            ReviewReaction existingReaction = optionalReviewReaction.get();
            if (existingReaction.getReaction() != reaction) {
                existingReaction.setReaction(reaction);
                reactionRepository.save(existingReaction);
            }

        } else {
            ReviewReaction reviewReaction = new ReviewReaction();
            reviewReaction.setUser(user);
            reviewReaction.setReview(review);
            reviewReaction.setReaction(reaction);
            reactionRepository.save(reviewReaction);

        }
    }

    public int getLikesCount(Review review) {
        return (int) review.getReactions().stream()
                .filter(r -> r.getReaction() == Reaction.LIKE)
                .count();
    }

    public int getDislikesCount(Review review) {
        return (int) review.getReactions().stream()
                .filter(r -> r.getReaction() == Reaction.DISLIKE)
                .count();
    }

}
