package com.example.hop_oasis.service.data;

import com.example.hop_oasis.convertor.ItemRatingMapper;
import com.example.hop_oasis.dto.ItemRatingDto;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleRating;
import com.example.hop_oasis.repository.ProductBundleRatingRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.service.ProductBundleRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductBundleRatingServiceImpl implements ProductBundleRatingService {
    private final ProductBundleRatingRepository bundleRatingRepository;
    private final ProductBundleRepository bundleRepository;
    private final ItemRatingMapper itemRatingMapper;

    @Transactional
    public void addRating(Long productBundleId, double ratingValue) {
        ProductBundle productBundle = bundleRepository.findById(productBundleId)
                .orElseThrow(() -> new IllegalArgumentException("Cider not found with id " + productBundleId));
        ProductBundleRating bundleRating = new ProductBundleRating();
        bundleRating.setProductBundle(productBundle);
        bundleRating.setRatingValue(ratingValue);
        bundleRatingRepository.save(bundleRating);
    }

    @Override
    public double getAverageRating(Long productBundleId) {
        List<ProductBundleRating> productBundleRatings = bundleRatingRepository.findByProductBundleId(productBundleId);
        return productBundleRatings.stream()
                .mapToDouble(ProductBundleRating::getRatingValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public int getRatingCount(Long productBundleId) {
        return bundleRatingRepository.countByProductBundleId(productBundleId);
    }

    @Override
    public ItemRatingDto getItemRating(Long productBundleId) {
        double averageRating = getAverageRating(productBundleId);
        int ratingCount = getRatingCount(productBundleId);
        return itemRatingMapper.toDto(productBundleId, averageRating, ratingCount);
    }

}
