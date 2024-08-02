package com.example.hop_oasis.convertor;

import java.util.List;

public interface Mappable<E, D> {

  D toDto(E entity);

  List<D> toDtos(List<E> entity);

  E toEntity(D dto);

  List<E> toEntities(List<D> dtos);

}
