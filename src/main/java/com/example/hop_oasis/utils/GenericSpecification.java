package com.example.hop_oasis.utils;

import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface GenericSpecification {
    static <T> Specification<T> IdsNotIn(Set<Long> exclusions) {
        return (root, query, cb) -> {
            if (exclusions == null || exclusions.isEmpty()) {
                return null;
            }

            return root.get("id").in(exclusions).not();
        };
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
