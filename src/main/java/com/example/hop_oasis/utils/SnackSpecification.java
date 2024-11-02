package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.model.SnackOptions;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class SnackSpecification {
    public static Specification<Snack> findByName(String snackName) {
        return (root, query, criteriaBuilder) ->
                snackName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("snackName"), snackName);
    }

    public static Specification<Snack> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<SnackOptions> snackOptionsRoot = subquery.from(SnackOptions.class);
            subquery.select(criteriaBuilder.max(snackOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(snackOptionsRoot.get("snack"), root));
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
