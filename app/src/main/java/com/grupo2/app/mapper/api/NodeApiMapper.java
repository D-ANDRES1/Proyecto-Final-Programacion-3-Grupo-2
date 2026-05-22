package com.grupo2.app.mapper.api;

import org.springframework.stereotype.Component;

import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.treeengine.domain.Node;


@Component
public class NodeApiMapper {

    public Node toDomain(CreateRootRequest request) {

        Node node = new Node();

        node.setValue(request.getValue());

        return node;
    }

    public Node toDomain(AddChildRequest request) {

        Node node = new Node();

        node.setValue(request.getValue());

        return node;
    }

    public NodeResponse toResponse(Node node) {

        NodeResponse response = new NodeResponse();

        response.setId(node.getId());

        response.setValue(node.getValue());

        if (node.getParentId() != null) {
            response.setParentId(node.getParentId());
        }
        
        if (node.getTreeId() != null) {
            response.setTreeId(node.getTreeId());
        }

        return response;
    }
}