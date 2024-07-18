package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ItemRatingMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderRating;
import com.example.hop_oasis.repository.CiderRatingRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.service.CiderRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CiderRatingServiceImpl implements CiderRatingService {
    private final CiderRatingRepository ciderRatingRepository;
    private final CiderRepository ciderRepository;
    private final ItemRatingMapper itemRatingMapper;

    @Transactional
    public void addRating(Long ciderId, double ratingValue) {
        Cider cider = ciderRepository.findById(ciderId)
                .orElseThrow(() -> new IllegalArgumentException("Cider not found with id " + ciderId));
        CiderRating ciderRating = new CiderRating();
        ciderRating.setCider(cider);
        ciderRating.setRatingValue(ratingValue);
        ciderRatingRepository.save(ciderRating);
    }

    @Override
    public double getAverageRating(Long ciderId) {
        List<CiderRating> ciderRatings = ciderRatingRepository.findByCiderId(ciderId);
        return ciderRatings.stream()
                .mapToDouble(CiderRating::getRatingValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public int getRatingCount(Long ciderId) {
        return ciderRatingRepository.countByCiderId(ciderId);
    }

    @Override
    public ItemRatingDto getItemRating(Long ciderId) {
        double averageRating = getAverageRating(ciderId);
        int ratingCount = getRatingCount(ciderId);
        return itemRatingMapper.toDto(ciderId, averageRating, ratingCount);
    }
}
