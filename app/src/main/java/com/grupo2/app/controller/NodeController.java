package com.grupo2.app.controller;

import com.grupo2.app.api.NodesApi;
import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.DepthResponse;
import com.grupo2.app.model.NodeListResponse;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.service.NodeService;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NodeController implements NodesApi {

    private final NodeService service;

    public NodeController(NodeService service) {
        this.service = service;
    }

    // =====================================================
    // CREATE ROOT
    // =====================================================
    @Override
    public ResponseEntity<NodeResponse> createRoot(CreateRootRequest body) {
        return ResponseEntity
                .status(201)
                .body(service.createRoot(body));
    }

    // =====================================================
    // ADD CHILD
    // =====================================================
    @Override
    public ResponseEntity<NodeResponse> addChild(
            UUID parentId,
            AddChildRequest body
    ) {
        return ResponseEntity
                .status(201)
                .body(service.addChild(parentId, body));
    }

    // =====================================================
    // GET ANCESTORS
    // =====================================================
    @Override
    public ResponseEntity<NodeListResponse> getAncestors(UUID nodeId) {
        return ResponseEntity.ok(service.getAncestors(nodeId));
    }

    // =====================================================
    // GET DEPTH
    // =====================================================
    @Override
    public ResponseEntity<DepthResponse> getDepth(UUID nodeId) {
        return ResponseEntity.ok(service.getDepth(nodeId));
    }

    // =====================================================
    // GET PATH TO ROOT
    // =====================================================
    @Override
    public ResponseEntity<NodeListResponse> getPathToRoot(UUID nodeId) {
        return ResponseEntity.ok(service.getPathToRoot(nodeId));
    }
}