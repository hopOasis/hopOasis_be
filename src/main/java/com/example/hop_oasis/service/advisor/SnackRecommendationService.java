package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.SNACK;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;

import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Component
class SnackRecommendationService implements RecommendationService {

    private final SnackRepository snackRepository;
    private final ProductBundleRepository bundleRepository;
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsByType) {
        var snackIds = productsByType.get(SNACK);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(snackIds)) {
            var randomSnacks = snackRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
            proposedProducts.setSnacks(randomSnacks);
            return proposedProducts;
        }

        var snackNames = snackRepository.findNamesByIds(snackIds);
        var recommendedBundles = bundleRepository.findAll(bundlesWithNamesLike(snackNames), PageRequest.of(0, 2))
                .getContent();
        proposedProducts.setBundles(recommendedBundles);

        var randomBeers = beerRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
        proposedProducts.setBeers(randomBeers);

        var randomCiders = ciderRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
        proposedProducts.setCiders(randomCiders);

        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return SNACK;
    }

}
