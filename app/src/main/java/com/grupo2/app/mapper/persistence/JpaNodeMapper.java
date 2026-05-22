package com.grupo2.app.mapper.persistence;

import org.springframework.stereotype.Component;

import com.grupo2.app.model.NodeEntity;
import com.grupo2.app.model.TreeEntity;
import com.grupo2.treeengine.domain.Node;

@Component
public class JpaNodeMapper
        implements PersistenceMapper<NodeEntity> {

    @Override
    public NodeEntity toEntity(Node node) {

        NodeEntity entity = new NodeEntity();

        // IMPORTANTE:
        // Ahora el engine genera IDs

        entity.setId(node.getId());

        entity.setValue(node.getValue());

        entity.setParentId(node.getParentId());

        // Asociar tree
        if (node.getTreeId() != null) {

            TreeEntity tree = new TreeEntity();

            tree.setId(node.getTreeId());

            entity.setTree(tree);
        }

        return entity;
    }

    @Override
    public Node toDomain(NodeEntity entity) {

        return new Node(
                entity.getId(),
                entity.getValue(),
                entity.getParentId(),
                entity.getTree() != null
                        ? entity.getTree().getId()
                        : null
        );
    }
}