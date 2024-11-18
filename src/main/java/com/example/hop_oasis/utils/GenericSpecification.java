package com.example.hop_oasis.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface GenericSpecification {

    static <T> Specification<T> idsNotIn(Set<Long> exclusions) {
        if (exclusions == null || exclusions.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        return Specification.not(idsIn(exclusions));
    }

    private static <T> Specification<T> idsIn(Set<Long> inclusions) {
        return (root, query, cb) -> {
            if (inclusions == null || inclusions.isEmpty()) {
                return cb.conjunction();
            }

            return root.get("id").in(inclusions);
        };
    }

    static <T> Specification<T> getRandomRecords() {
        return (root, query, cb) -> {
            if (query != null) {
                query.orderBy(cb.asc(cb.function("RAND", null)));
            }

            return cb.conjunction();
        };
    }

}
