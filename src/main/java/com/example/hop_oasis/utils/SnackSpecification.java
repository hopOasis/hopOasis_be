package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackOptions;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SnackSpecification {
    public static Specification<Snack> filterAndSort(String name, String sortDirection) {
        return SnackSpecification.findByName(name).and(SnackSpecification.sortByPrice(sortDirection));

    }

    private static Specification<Snack> findByName(String snackName) {
        return (root, query, criteriaBuilder) ->
                snackName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("snackName"), snackName);
    }

    private static Specification<Snack> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<SnackOptions> snacksOptionsRoot = subquery.from(SnackOptions.class);
            subquery.select(criteriaBuilder.max(snacksOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(snacksOptionsRoot.get("snack"), root));
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
