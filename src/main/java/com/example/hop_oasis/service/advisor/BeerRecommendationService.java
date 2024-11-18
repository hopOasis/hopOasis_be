package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.BEER;
import static com.example.hop_oasis.utils.BeerSpecification.beerWithTheSameColors;
import static com.example.hop_oasis.utils.GenericSpecification.*;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
                        Specification.allOf(beerWithTheSameColors(beerIds), idsNotIn(beerIds)), PageRequest.of(0, 2))
                .getContent();
        proposedProducts.setBeers(recommendedBeers);

        var beerNames = beerRepository.findNamesByIds(beerIds);

        var recommendedBundles = bundleRepository.findAll(bundlesWithNamesLike(beerNames), PageRequest.of(0, 2))
                .getContent();
        proposedProducts.setBundles(recommendedBundles);

        var recommendedSnacks = snackRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
        proposedProducts.setSnacks(recommendedSnacks);

        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return BEER;
    }

}
