package com.example.hop_oasis.service.data;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.function.Function;
@RequiredArgsConstructor
public abstract class GeneralFilterService<T, D> {
    protected Page<D> getAllWithFilter(
            Specification<T> specification,
            JpaSpecificationExecutor<T> repository,
            Pageable pageable,
            Function<T, D> toDtoMapper) {

        Page<T> entities = repository.findAll(specification, pageable);
        return entities.map(toDtoMapper);
    }

}
