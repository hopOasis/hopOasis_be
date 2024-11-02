package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleOptions;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

public class ProductBundleSpecification {
    public static Specification<ProductBundle> findByName(String bundleName) {
        return (root, query, criteriaBuilder) ->
                bundleName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("name"), bundleName);
    }

    public static Specification<ProductBundle> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<ProductBundleOptions> bundleOptionsRoot = subquery.from(ProductBundleOptions.class);
            subquery.select(criteriaBuilder.max(bundleOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(bundleOptionsRoot.get("productBundle"), root));
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
