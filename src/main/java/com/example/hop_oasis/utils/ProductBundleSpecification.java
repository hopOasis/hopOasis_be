package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.model.ProductBundleOptions;
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

    public static Specification<ProductBundle> pbWithNameLike(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    private static Specification<ProductBundle> findByName(String bundleName) {
        return (root, query, criteriaBuilder) ->
                bundleName == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("name"), bundleName);
    }

    private static Specification<ProductBundle> sortByPrice(String sortDirection) {
        return (root, query, criteriaBuilder) -> {
            Subquery<Double> subquery = query.subquery(Double.class);
            Root<ProductBundleOptions> pbOptionsRoot = subquery.from(ProductBundleOptions.class);
            subquery.select(criteriaBuilder.max(pbOptionsRoot.get("price")))
                    .where(criteriaBuilder.equal(pbOptionsRoot.get("productBundle"), root));
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
