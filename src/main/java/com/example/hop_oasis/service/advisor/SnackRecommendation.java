package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.BEER;
import static com.example.hop_oasis.utils.GenericSpecification.idsIn;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.SnackRepository;
import com.example.hop_oasis.utils.GenericSpecification;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
class SnackRecommendation implements Recommendation {

    private final SnackRepository snackRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsByType) {
        var snackIds = productsByType.get(BEER);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(snackIds)) {
            var randomSnacks = snackRepository.findAll(GenericSpecification.getRandomRecords());
            proposedProducts.setSnacks(randomSnacks);
            return proposedProducts;
        }

        final var recommendedSnacks = snackRepository.findAll(idsIn(snackIds));
        proposedProducts.setSnacks(recommendedSnacks);
        return proposedProducts;
    }

}
