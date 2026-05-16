package com.grupo2.app.mapper;

import org.springframework.stereotype.Component;

import com.grupo2.app.model.NodeEntity;
import com.grupo2.treeengine.core.TreeNode;

@Component
public class RequestMapper {

    public NodeEntity toEntity(TreeNode node) {
        if (node == null) return null;
        
        NodeEntity entity = new NodeEntity();
        // NO ponemos ID porque JPA lo genera automático
        entity.setValue(node.getValue());
        // parentId y depth NO van aquí, se ponen después en la estrategia
        return entity;
    }
    
    public TreeNode createTreeNode(String id, String value) {
        return new TreeNode(id, value);
    }
}