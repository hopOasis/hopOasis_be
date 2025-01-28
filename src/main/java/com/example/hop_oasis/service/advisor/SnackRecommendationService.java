package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
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

        var randomBeers = beerRepository.findAll(allOf(idsNotIn(productsByType.get(BEER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setBeers(randomBeers);

        var randomCiders = ciderRepository.findAll(allOf(idsNotIn(productsByType.get(CIDER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setCiders(randomCiders);

        var snackNames = snackRepository.findNamesByIds(snackIds);
        var recommendedBundles = bundleRepository.findAll(
                allOf(bundlesWithNamesLike(snackNames), idsNotIn(productsByType.get(PRODUCT_BUNDLE))),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setBundles(recommendedBundles);


        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return SNACK;
    }

}
