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

    public Review createReview(ReviewDto reviewDto) {
        if (reviewDto.getItemId() == null || reviewDto.getItemType() == null) {
            throw new IllegalArgumentException("ItemId and ItemType cannot be null");
        }
        User user = userRepository.findById(reviewDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + reviewDto.getUserId(), ""));
        validateItemExists(reviewDto.getItemId(), reviewDto.getItemType());
        Review review = reviewMapper.toEntity(reviewDto);
        review.setUser(user);
        return reviewRepository.save(review);
    }

    public ReviewInfoDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id, ""));
        return reviewMapper.toReviewInfoDto(review);

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
