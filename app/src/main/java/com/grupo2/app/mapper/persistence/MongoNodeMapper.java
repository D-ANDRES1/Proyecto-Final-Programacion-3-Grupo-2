package com.grupo2.app.mapper.persistence;

import org.springframework.stereotype.Component;

import com.grupo2.app.model.MongoNode;
import com.grupo2.treeengine.domain.Node;

import java.util.UUID;

@Component
public class MongoNodeMapper
        implements PersistenceMapper<MongoNode> {

    @Override
    public MongoNode toEntity(Node node) {

        MongoNode mongo = new MongoNode();

        mongo.setId(
                node.getId().toString()
        );

        mongo.setValue(
                node.getValue()
        );

        mongo.setParentId(
                node.getParentId() != null
                        ? node.getParentId().toString()
                        : null
        );

        mongo.setTreeId(
                node.getTreeId() != null
                        ? node.getTreeId().toString()
                        : null
        );

        return mongo;
    }

    @Override
    public Node toDomain(MongoNode mongo) {

        return new Node(
                UUID.fromString(mongo.getId()),
                mongo.getValue(),
                mongo.getParentId() != null
                        ? UUID.fromString(mongo.getParentId())
                        : null,
                mongo.getTreeId() != null
                        ? UUID.fromString(mongo.getTreeId())
                        : null
        );
    }
}