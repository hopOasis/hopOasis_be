package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.CiderOptions;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class CiderSpecification {
    public static Specification<Cider> findByName(String ciderName) {
        return (root, query, criteriaBuilder) ->
                ciderName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("ciderName"), ciderName);
    }
    public static Specification<Cider> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<CiderOptions> ciderOptionsRoot = subquery.from(CiderOptions.class);
            subquery.select(criteriaBuilder.max(ciderOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(ciderOptionsRoot.get("cider"), root));
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
