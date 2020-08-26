package com.backend.common;

import java.util.List;

public interface EntityConverter<E, D> {

    E toEntity(D d);

    D toEntityDto(E e);

    List<E> toEntity(List<D> dtoList);

    List<D> toEntityDto(List<E> entityList);

}
