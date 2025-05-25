package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ReviewMapper;
import com.example.hop_oasis.dto.ReviewDto;
import com.example.hop_oasis.dto.ReviewInfoDto;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Review;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository bundleRepository;
    private final UserRepository userRepository;
    private final ReviewReactionService reactionService;

    public Review createReview(ReviewDto reviewDto) {
        if (reviewDto.getItemId() == null || reviewDto.getItemType() == null) {
            throw new IllegalArgumentException("ItemId and ItemType cannot be null");
        } else if (reviewDto.getContent() == null || reviewDto.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Field with content can not be empty or null", "");
        } else if (reviewDto.getContent().length() > 500) {
            throw new IllegalArgumentException("Allowed up to 500 symbols");

        }
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reviewDto.getUserId(), ""));
        validateItemExists(reviewDto.getItemId(), reviewDto.getItemType());
        Review review = reviewMapper.toEntity(reviewDto);
        review.setUser(user);
        return reviewRepository.save(review);
    }

    public List<ReviewInfoDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::countReactions).toList();
    }

    public ReviewInfoDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id, ""));
        return countReactions(review);

    }

    private ReviewInfoDto countReactions(Review review) {
        ReviewInfoDto dto = reviewMapper.toReviewInfoDto(review);
        dto.setLikes(reactionService.getLikesCount(review));
        dto.setDislikes(reactionService.getDislikesCount(review));
        return dto;

    }

    public ReviewInfoDto deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id, ""));
        reviewRepository.deleteById(id);
        return reviewMapper.toReviewInfoDto(review);

    }

    private void validateItemExists(Long itemId, ItemType itemType) {
        boolean exists = switch (itemType) {
            case BEER -> beerRepository.existsById(itemId);
            case CIDER -> ciderRepository.existsById(itemId);
            case SNACK -> snackRepository.existsById(itemId);
            case PRODUCT_BUNDLE -> bundleRepository.existsById(itemId);
        };
        if (!exists) {
            throw new ResourceNotFoundException(
                    "Item of type " + itemType + " with id " + itemId + " does not exist", "");
        }
    }
}
