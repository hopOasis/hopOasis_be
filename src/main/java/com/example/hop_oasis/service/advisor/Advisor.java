package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Advisor {
    private final ProductBundleRepository productBundleRepository;
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;

    public Recommendation forProduct(ItemType itemType) {
        return switch (itemType) {
            case BEER -> new BeerRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository);
            case CIDER -> new CiderRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository);
            case SNACK -> new SnackRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository);
            case PRODUCT_BUNDLE -> new ProductBundleRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository);
        };
    }
}
