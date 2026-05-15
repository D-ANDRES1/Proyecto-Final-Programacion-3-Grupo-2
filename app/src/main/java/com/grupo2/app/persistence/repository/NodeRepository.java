package com.grupo2.app.persistence.repository;

import com.grupo2.app.model.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para NodeEntity.
 * Proporciona métodos CRUD automáticos.
 */
@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, String> {
    
    /**
     * Buscar hijos por ID del padre
     */
    List<NodeEntity> findByParentId(String parentId);
}