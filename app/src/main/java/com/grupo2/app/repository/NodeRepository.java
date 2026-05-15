package com.grupo2.app.repository;

import com.grupo2.app.model.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para NodeEntity.
 * Compatible con NodeService existente.
 */
@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, String> {
    
    /**
     * Buscar hijos por ID del padre
     */
    List<NodeEntity> findByParentId(String parentId);
    
    /**
     * Verificar si ya existe un nodo raíz (parentId es null)
     * 👈 ESTE MÉTODO ES EL QUE USA TU NodeService
     */
    boolean existsByParentIdIsNull();
    
    /**
     * Buscar el nodo raíz (opcional)
     */
    Optional<NodeEntity> findByParentIdIsNull();
}