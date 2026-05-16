package com.grupo2.app.mapper;

import com.grupo2.app.model.MongoNode;
import com.grupo2.treeengine.core.TreeNode;
import org.springframework.stereotype.Component;

/**
 * Mapper específico para MongoDB: MongoNode ↔ TreeNode
 */
@Component
public class MongoMapper {

    /**
     * MongoNode → TreeNode
     */
    public TreeNode toDomain(MongoNode mongoNode) {
        if (mongoNode == null) return null;
        return new TreeNode(mongoNode.getId(), mongoNode.getValue());
    }

    /**
     * TreeNode → MongoNode
     */
    public MongoNode toMongoNode(TreeNode node) {
        if (node == null) return null;
        MongoNode mongoNode = new MongoNode();
        mongoNode.setId(node.getId());
        mongoNode.setValue(node.getValue());
        return mongoNode;
    }
}