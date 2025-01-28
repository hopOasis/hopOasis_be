package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

import static com.example.hop_oasis.model.ItemType.*;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static com.example.hop_oasis.utils.GenericSpecification.idsNotIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;
import static org.springframework.data.jpa.domain.Specification.allOf;

@RequiredArgsConstructor
@Component
class CiderRecommendationService implements RecommendationService {

    private final CiderRepository ciderRepository;
    private final ProductBundleRepository bundleRepository;
    private final SnackRepository snackRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsByType) {
        var ciderIds = productsByType.get(CIDER);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(ciderIds)) {
            var randomCiders = ciderRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
            proposedProducts.setCiders(randomCiders);
            return proposedProducts;
        }

        var recommendedSnacks = snackRepository.findAll(allOf(idsNotIn(productsByType.get(SNACK)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setSnacks(recommendedSnacks);

        var ciderNames = ciderRepository.findNamesByIds(ciderIds);

        var recommendedBundles = bundleRepository.findAll(
                allOf(bundlesWithNamesLike(ciderNames), idsNotIn(productsByType.get(PRODUCT_BUNDLE))),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setBundles(recommendedBundles);

        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return CIDER;
    }

}
