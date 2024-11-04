package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.BeerOptions;
import jakarta.persistence.criteria.Join;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeerSpecification {

    public static Specification<Beer> filterAndSort(String name, String sortDirection) {
        return BeerSpecification.findByName(name).and(BeerSpecification.sortByPrice(sortDirection));
    }

    private static Specification<Beer> findByName(String beerName) {
        return (root, query, criteriaBuilder) ->
            beerName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("beerName"), beerName);
    }

    private static Specification<Beer> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            if (sortDirection == null || sortDirection.isBlank()) {
                query.orderBy(criteriaBuilder.asc(root.get("id")));
                return criteriaBuilder.conjunction();
            }

            Join<Beer, BeerOptions> beerOptionsJoin = root.join("beerOptions");
            if (sortDirection.equalsIgnoreCase("asc")) {
                query.orderBy(criteriaBuilder.asc(beerOptionsJoin.get("price")));
            } else if (sortDirection.equalsIgnoreCase("desc")) {
                query.orderBy(criteriaBuilder.desc(beerOptionsJoin.get("price")));
            }

            return criteriaBuilder.conjunction();
        };
    }

}
