package com.grupo2.app.service;

import com.grupo2.app.mapper.api.NodeApiMapper;
import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.service.TreeService;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NodeService {

    private final TreeService treeService;

    private final NodeApiMapper mapper;
    
    private final PersistenceStrategy persistence;

    public NodeService(
            TreeService treeService,
            NodeApiMapper mapper,
            PersistenceStrategy persistence
    ) {
        this.treeService = treeService;
        this.mapper = mapper;
        this.persistence = persistence;
    }

    // =========================
    // CREATE ROOT
    // =========================

    public NodeResponse createRoot(CreateRootRequest request) {

        Node domainNode = mapper.toDomain(request);

        Node computedNode = treeService.createRoot(domainNode);

        Node savedNode = persistence.save(computedNode);

        return mapper.toResponse(savedNode);
    }

    // =========================
    // ADD CHILD
    // =========================

    public NodeResponse addChild(
            UUID parentId,
            AddChildRequest request
    ) {

        Node domainNode = mapper.toDomain(request);

        Node computedNode = treeService.addChild(parentId, domainNode);

        Node savedNode = persistence.save(computedNode);

        return mapper.toResponse(savedNode);
    }
}