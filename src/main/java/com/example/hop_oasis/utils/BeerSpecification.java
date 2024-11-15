package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.BeerOptions;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeerSpecification {

    public static Specification<Beer> filterAndSort(String name, String sortDirection) {
        return BeerSpecification.findByName(name).and(BeerSpecification.sortByPrice(sortDirection));
    }

    public static Specification<Beer> beerWithTheSameColors(Collection<Long> ids) {
        return (Root<Beer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {

            Subquery<String> subquery = query.subquery(String.class);
            Root<Beer> subRoot = subquery.from(Beer.class);
            subquery.select(subRoot.get("beerColor")).distinct(true)
                .where(subRoot.get("id").in(ids));

            return root.get("beerColor").in(subquery);
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
