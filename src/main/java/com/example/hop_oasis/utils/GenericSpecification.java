package com.example.hop_oasis.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface GenericSpecification {

    static <T> Specification<T> IdsIn(Set<Long> inclusions) {
        return (root, query, cb) -> {
            if (inclusions == null || inclusions.isEmpty()) {
                return null;
            }

            return root.get("id").in(inclusions);
        };
    }

    static <T> Specification<T> IdsNotIn(Set<Long> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return null;
        }
        return Specification.not(IdsIn(exclusions));
    }

    static  <T> Specification<T> getRandomRecords() {

        return (root, query, cb) -> {
            if (query != null) {
                query.orderBy(cb.asc(cb.function("RAND", null)));
            }

            return null;
        };
    }
}
