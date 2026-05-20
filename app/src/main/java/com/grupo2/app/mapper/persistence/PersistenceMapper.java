package com.grupo2.app.mapper.persistence;

import com.grupo2.treeengine.domain.Node;

public interface PersistenceMapper<E> {

    E toEntity(Node node);

    Node toDomain(E entity);
}