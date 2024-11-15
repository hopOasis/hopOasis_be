package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.BEER;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.utils.BeerSpecification;
import com.example.hop_oasis.utils.GenericSpecification;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
class BeerRecommendation implements Recommendation {

    private final BeerRepository beerRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsByType) {
        var beerIds = productsByType.get(BEER);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(beerIds)) {
            var randomBeers = beerRepository.findAll(GenericSpecification.getRandomRecords());
            proposedProducts.setBeers(randomBeers);
            return proposedProducts;
        }

        var recommendedBeers = beerRepository.findAll(BeerSpecification.beerWithTheSameColors(beerIds));
        proposedProducts.setBeers(recommendedBeers);
        return proposedProducts;
    }

}
