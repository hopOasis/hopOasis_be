package com.example.hop_oasis.service.data;

import com.example.hop_oasis.enums.Reaction;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.model.ReviewReaction;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.ReviewReactionRepository;
import com.example.hop_oasis.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewReactionService {

    private final ReviewRepository reviewRepository;
    private final ReviewReactionRepository reactionRepository;
    private final UserAuthenticated userAuthenticated;

    @Transactional
    public void addReaction(Long reviewId, Reaction reaction) {
        User authUser = userAuthenticated.getAuthenticatedUser();
        User proxyUser = new User();
        proxyUser.setId(authUser.getId());
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(() -> new IllegalArgumentException("Review not found with id"));
        Optional<ReviewReaction> optionalReviewReaction = reactionRepository.findByUserAndReview(proxyUser, review);
        if (optionalReviewReaction.isPresent()) {
            ReviewReaction existingReaction = optionalReviewReaction.get();
            if (existingReaction.getReaction() != reaction) {
                existingReaction.setReaction(reaction);
                reactionRepository.save(existingReaction);
            }

        } else {
            ReviewReaction reviewReaction = new ReviewReaction();
            reviewReaction.setUser(proxyUser);
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
