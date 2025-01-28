package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

import static com.example.hop_oasis.model.ItemType.*;
import static com.example.hop_oasis.utils.BeerSpecification.beerWithTheSameColors;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static com.example.hop_oasis.utils.GenericSpecification.idsNotIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;
import static org.springframework.data.jpa.domain.Specification.allOf;

@RequiredArgsConstructor
@Component
class BeerRecommendationService implements RecommendationService {

    private final BeerRepository beerRepository;
    private final ProductBundleRepository bundleRepository;
    private final SnackRepository snackRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsByType) {
        var beerIds = productsByType.get(BEER);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(beerIds)) {
            var randomBeers = beerRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
            proposedProducts.setBeers(randomBeers);

            return proposedProducts;
        }

        var recommendedBeers = beerRepository.findAll(
                allOf(beerWithTheSameColors(beerIds), idsNotIn(beerIds)), PageRequest.of(0, 2)).getContent();
        proposedProducts.setBeers(recommendedBeers);

        var beerNames = beerRepository.findNamesByIds(beerIds);

        var recommendedBundles = bundleRepository.findAll(
                allOf(bundlesWithNamesLike(beerNames), idsNotIn(productsByType.get(PRODUCT_BUNDLE))),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setBundles(recommendedBundles);

        var recommendedSnacks = snackRepository.findAll(allOf(idsNotIn(productsByType.get(SNACK)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setSnacks(recommendedSnacks);

        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return BEER;
    }

}
