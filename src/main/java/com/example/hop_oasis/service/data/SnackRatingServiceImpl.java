package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ItemRatingMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackRating;
import com.example.hop_oasis.repository.SnackRatingRepository;
import com.example.hop_oasis.repository.SnackRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnackRatingServiceImpl {
    private final SnackRatingRepository snackRatingRepository;
    private final SnackRepository snackRepository;
    private final ItemRatingMapper itemRatingMapper;

    @Transactional
    public void addRating(Long snackId, double ratingValue) {
        Snack snack = snackRepository.findById(snackId)
                .orElseThrow(() -> new IllegalArgumentException("Snack not found with id " + snackId));
        SnackRating snackRating = new SnackRating();
        snackRating.setSnack(snack);
        snackRating.setRatingValue(ratingValue);
        snackRatingRepository.save(snackRating);
    }

    public double getAverageRating(Long snackId) {
        List<SnackRating> snackRatings = snackRatingRepository.findBySnackId(snackId);
        return snackRatings.stream()
                .mapToDouble(SnackRating::getRatingValue)
                .average()
                .orElse(0.0);
    }

    public int getRatingCount(Long snackId) {
        return snackRatingRepository.countBySnackId(snackId);
    }


    public ItemRatingDto getItemRating(Long snackId) {
        double averageRating = getAverageRating(snackId);
        int ratingCount = getRatingCount(snackId);
        return itemRatingMapper.toDto(snackId, averageRating, ratingCount);
    }

}
