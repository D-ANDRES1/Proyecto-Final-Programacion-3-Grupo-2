package com.grupo2.app.mapper.persistence;

import com.grupo2.app.model.MongoNode;
import com.grupo2.treeengine.domain.Node;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MongoNodeMapper
        implements PersistenceMapper<MongoNode> {

    @Override
    public MongoNode toEntity(Node node) {

        MongoNode mongoNode = new MongoNode();

        // Mongo usa String
        if (node.getId() != null) {
            mongoNode.setId(node.getId().toString());
        }

        mongoNode.setValue(node.getValue());

        if (node.getParentId() != null) {
            mongoNode.setParentId(node.getParentId().toString());
        }

        if (node.getTreeId() != null) {
            mongoNode.setTreeId(node.getTreeId().toString());
        }

        return mongoNode;
    }

    @Override
    public Node toDomain(MongoNode mongoNode) {

        UUID id = mongoNode.getId() != null
                ? UUID.fromString(mongoNode.getId())
                : null;

        UUID parentId = mongoNode.getParentId() != null
                ? UUID.fromString(mongoNode.getParentId())
                : null;

        UUID treeId = mongoNode.getTreeId() != null
                ? UUID.fromString(mongoNode.getTreeId())
                : null;

        return new Node(
                id,
                mongoNode.getValue(),
                parentId,
                treeId
        );
    }
}