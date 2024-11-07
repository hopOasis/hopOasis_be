package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.CiderOptions;
import com.example.hop_oasis.model.ProductBundle;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ProductBundleSpecification {
    public static Specification<ProductBundle> filterAndSort(String name, String sortDirection) {
        return ProductBundleSpecification.findByName(name).and(ProductBundleSpecification.sortByPrice(sortDirection));

    }

    private static Specification<ProductBundle> findByName(String bundleName) {
        return (root, query, criteriaBuilder) ->
                bundleName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("name"), bundleName);
    }

    private static Specification<ProductBundle> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<CiderOptions> ciderOptionsRoot = subquery.from(CiderOptions.class);
            subquery.select(criteriaBuilder.max(ciderOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(ciderOptionsRoot.get("productBundle"), root));
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
