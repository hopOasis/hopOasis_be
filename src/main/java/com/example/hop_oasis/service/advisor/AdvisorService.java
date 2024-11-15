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
public class AdvisorService {

    private final ProductBundleRepository productBundleRepository;

    private final BeerRepository beerRepository;

    private final CiderRepository ciderRepository;

    private final SnackRepository snackRepository;

    public Recommendation forProduct(ItemType itemType) {
        return switch (itemType) {
            case BEER -> new BeerRecommendation(beerRepository);
            case CIDER -> new CiderRecommendation(ciderRepository);
            case SNACK -> new SnackRecommendation(snackRepository);
            case PRODUCT_BUNDLE -> new ProductBundleRecommendation(productBundleRepository);
        };
    }

}
