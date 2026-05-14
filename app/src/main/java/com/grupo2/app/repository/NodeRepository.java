package com.grupo2.app.repository;

import com.grupo2.app.model.NodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends JpaRepository<NodeEntity, String> {

	Optional<NodeEntity> findById(String id);
    List<NodeEntity> findByParentId(String parentId);
    boolean existsByParentIdIsNull();
    Optional<NodeEntity> findByParentIdIsNull();
    
    
    
}