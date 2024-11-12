package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.Snack;
import org.springframework.data.jpa.domain.Specification;

public record RecommendationPredicates(
        Specification<Beer> beerSpecs,
        Specification<Cider> ciderSpecs,
        Specification<Snack> snackSpecs,
        Specification<ProductBundle> pbSpecs
) {
}
