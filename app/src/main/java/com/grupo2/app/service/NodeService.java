package com.grupo2.app.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.grupo2.app.mapper.api.NodeApiMapper;
import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.service.TreeService;

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
    // 1. CREATE ROOT
    // =========================
    public NodeResponse createRoot(CreateRootRequest request) {
        Node domainNode = mapper.toDomain(request);
        Node computedNode = treeService.createRoot(domainNode);
        Node savedNode = persistence.save(computedNode);
        return mapper.toResponse(savedNode);
    }

    // =========================
    // 2. ADD CHILD
    // =========================
    public NodeResponse addChild(UUID parentId, AddChildRequest request) {
        Node domainNode = mapper.toDomain(request);
        Node computedNode = treeService.addChild(parentId, domainNode);
        Node savedNode = persistence.save(computedNode);
        return mapper.toResponse(savedNode);
    }

    // =========================
    // 3. GET FULL TREE
    // =========================
    public TreeView getTree() {
        return treeService.getTree();
    }

    // =========================
    // 4. GET SUBTREE
    // =========================
    public TreeView getSubTree(UUID nodeId) {
        return treeService.getSubTree(nodeId);
    }

    // =========================
    // 5. GET PATH TO ROOT
    // =========================
    public List<Node> getPathToRoot(UUID nodeId) {
        return treeService.getPathToRoot(nodeId);
    }

    // =========================
    // 6. DFS TRAVERSAL
    // =========================
    public List<Node> dfs() {
        return treeService.dfs();
    }

    // =========================
    // 7. BFS TRAVERSAL
    // =========================
    public List<Node> bfs() {
        return treeService.bfs();
    }

    // =========================
    // 8. GET HEIGHT
    // =========================
    public int getHeight() {
        return treeService.getHeight();
    }

    // =========================
    // 9. GET DEPTH
    // =========================
    public int getDepth(UUID nodeId) {
        return treeService.getDepth(nodeId);
    }

    // =========================
    // 10. GET ANCESTORS
    // =========================
    public List<Node> getAncestors(UUID nodeId) {
        return treeService.getAncestors(nodeId);
    }

    // =========================
    // 11. VALIDATE NO CYCLES
    // =========================
    public boolean validateNoCycles() {
        return treeService.validateNoCycles();
    }
}