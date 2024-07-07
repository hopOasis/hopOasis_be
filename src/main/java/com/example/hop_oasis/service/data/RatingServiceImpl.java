package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Rating;
import com.example.hop_oasis.repository.RatingRepository;
import com.example.hop_oasis.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;

    @Override
    public double getAverageRating(Long itemId, ItemType itemType){
        List<Rating> ratings = ratingRepository.findByItemIdAndItemType(itemId, itemType);
        return ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
    }
    @Override
    public int getRatingCount(Long itemId, ItemType itemType){
       return ratingRepository.countByItemIdAndItemType(itemId, itemType);
    }
    @Override
    public ItemRatingDto getItemRating(Long itemId, ItemType itemType){
        double averageRating = getAverageRating(itemId, itemType);
        int ratingCount = getRatingCount(itemId, itemType);
        return new ItemRatingDto(itemId, itemType, averageRating, ratingCount);
    }

}
