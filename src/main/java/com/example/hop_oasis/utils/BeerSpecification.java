package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.BeerOptions;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeerSpecification {
    public static Specification<Beer> filterAndSort(String name, String sortDirection) {
        return BeerSpecification.findByName(name).and(BeerSpecification.sortByPrice(sortDirection));
    }

    public static Specification<Beer> beerWithTheSameColors(Set<String> colors) {
        return (root, query, cb) -> {
            if (colors == null || colors.isEmpty()) {
                return null;
            }

            final var pArray = colors.stream()
                    .map(c -> cb.equal(root.get("beerColor"), c))
                    .toList().toArray(new Predicate[0]);

            return cb.or(pArray);
        };
    }

    private static Specification<Beer> findByName(String beerName) {
        return (root, query, criteriaBuilder) ->
                beerName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("beerName"), beerName);
    }

    private static Specification<Beer> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<BeerOptions> beerOptionsRoot = subquery.from(BeerOptions.class);
            subquery.select(criteriaBuilder.max(beerOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(beerOptionsRoot.get("beer"), root));
            if (sortDirection == null) {
                query.orderBy(criteriaBuilder.asc(root.get("id")));
            } else if (sortDirection.equalsIgnoreCase("asc")) {
                query.orderBy(criteriaBuilder.asc(subquery));
            } else if (sortDirection.equalsIgnoreCase("desc")) {
                query.orderBy(criteriaBuilder.desc(subquery));
            }
            return criteriaBuilder.conjunction();
        };
    }
}
