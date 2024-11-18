package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.BEER;
import static com.example.hop_oasis.model.ItemType.CIDER;
import static com.example.hop_oasis.model.ItemType.PRODUCT_BUNDLE;
import static com.example.hop_oasis.model.ItemType.SNACK;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.utils.ProductBundleSpecification;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductBundleRecommendationService implements RecommendationService {

    private final ProductBundleRepository productBundleRepository;

    private final BeerRepository beerRepository;

    private final SnackRepository snackRepository;

    private final CiderRepository ciderRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsMap) {
        var productNames = new HashSet<String>();
        productNames.addAll(snackRepository.findNamesByIds(productsMap.get(SNACK)));
        productNames.addAll(beerRepository.findNamesByIds(productsMap.get(BEER)));
        productNames.addAll(ciderRepository.findNamesByIds(productsMap.get(CIDER)));
        productNames.addAll(productBundleRepository.findNamesByIds(productsMap.get(PRODUCT_BUNDLE)));
        var recommendedProductBundles = productBundleRepository.findAll(ProductBundleSpecification.bundlesWithNamesLike(productNames));

        return new ProposedProducts(
            null,
            null,
            null,
            recommendedProductBundles
        );
    }

    @Override
    public ItemType supportedItemType() {
        return PRODUCT_BUNDLE;
    }

}
