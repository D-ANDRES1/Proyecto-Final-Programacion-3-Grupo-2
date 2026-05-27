package com.grupo2.app.controller;

import com.grupo2.app.api.TreeApi;
import com.grupo2.app.model.HeightResponse;

import com.grupo2.app.model.NodeListResponse;
import com.grupo2.app.model.TreeListResponse;
import com.grupo2.app.model.TreeResponse;
import com.grupo2.app.model.ValidationResponse;
import com.grupo2.app.service.NodeService;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TreeController implements TreeApi {

    private final NodeService service;

    public TreeController(
            NodeService service
    ) {

        this.service = service;
    }

    // =====================================================
    // GET ALL TREES
    // =====================================================

    @Override
    public ResponseEntity<TreeListResponse> getTree() {

        TreeListResponse response =
                new TreeListResponse();

        response.setItems(
                service.getTrees()
        );

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // GET SUBTREE
    // =====================================================

    @Override
    public ResponseEntity<TreeResponse> getSubTree(
            UUID nodeId
    ) {

        return ResponseEntity.ok(
                service.getSubTree(nodeId)
        );
    }

    // =====================================================
    // PLACEHOLDERS
    // =====================================================

    @Override
    public ResponseEntity<HeightResponse> getHeight() {
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<NodeListResponse> traversal(String type) {
        return ResponseEntity.ok(
                service.traversal(type)
        );
    }

    @Override
    public ResponseEntity<ValidationResponse> validateNoCycles() {
        return ResponseEntity.notFound().build();
    }
}