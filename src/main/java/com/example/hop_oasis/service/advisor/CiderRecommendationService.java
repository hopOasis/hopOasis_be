package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.CIDER;
import static com.example.hop_oasis.utils.GenericSpecification.idsNotIn;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.utils.GenericSpecification;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Component
class CiderRecommendationService implements RecommendationService {

    private final CiderRepository ciderRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsByType) {
        var ciderIds = productsByType.get(CIDER);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(ciderIds)) {
            var randomCiders = ciderRepository.findAll(GenericSpecification.getRandomRecords());
            proposedProducts.setCiders(randomCiders);
            return proposedProducts;
        }
        var ciderRecommendations = ciderRepository.findAll(idsNotIn(ciderIds));
        proposedProducts.setCiders(ciderRecommendations);
        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return CIDER;
    }

}
