package com.grupo2.app.persistence.strategy;


import java.util.List;
import java.util.Optional;

import com.grupo2.treeengine.core.TreeNode;

/**
 * Interfaz que define cómo persistir nodos.
 * Todas las estrategias (JPA, Mongo, Memoria) deben implementarla.
 */
public interface PersistenceStrategy {
    
    TreeNode createRoot(TreeNode node);
    
    TreeNode addChild(String parentId, TreeNode childNode);
    
    Optional<TreeNode> findById(String id);
    
    List<TreeNode> findAll();
    
    List<TreeNode> findChildren(String parentId);
    
    void delete(String id);
}