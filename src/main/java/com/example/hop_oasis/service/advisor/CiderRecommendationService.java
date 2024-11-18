package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.CIDER;
import static com.example.hop_oasis.utils.GenericSpecification.*;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;

import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
            var randomCiders = ciderRepository.findAll(getRandomRecords());
            proposedProducts.setCiders(randomCiders);
            return proposedProducts;
        }

        var recommendedSnacks = snackRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
        proposedProducts.setSnacks(recommendedSnacks);

        var ciderNames = ciderRepository.findAll(idsIn(ciderIds)).stream()
                .map(Cider::getCiderName)
                .collect(Collectors.toSet());

        var recommendedBundles = bundleRepository.findAll(bundlesWithNamesLike(ciderNames), PageRequest.of(0, 2))
                .getContent();
        proposedProducts.setBundles(recommendedBundles);

        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return CIDER;
    }

}
