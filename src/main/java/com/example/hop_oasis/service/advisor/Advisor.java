package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Advisor {
    private final ProductBundleRepository productBundleRepository;
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;

    public Recommendation forProduct(ItemType itemType) {
        return forProduct(itemType, Map.of());
    }

    public Recommendation forProduct(ItemType itemType, Map<ItemType, Set<Long>> exclustionMap) {
        return switch (itemType) {
            case BEER -> new BeerRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository, exclustionMap);
            case CIDER -> new CiderRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository, exclustionMap);
            case SNACK -> new SnackRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository, exclustionMap);
            case PRODUCT_BUNDLE -> new ProductBundleRecommendation(
                    beerRepository, ciderRepository, snackRepository, productBundleRepository, exclustionMap);
        };
    }
}
