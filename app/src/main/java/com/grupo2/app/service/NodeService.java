package com.grupo2.app.service;

import com.grupo2.app.mapper.api.NodeApiMapper;
import com.grupo2.app.mapper.api.TreeViewApiMapper;
import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.model.TreeResponse;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.service.TreeService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NodeService {

    private final TreeService treeService;

    private final NodeApiMapper mapper;
    
    private final TreeViewApiMapper treeViewMapper;
    
    private final PersistenceStrategy persistence;

    public NodeService(
            TreeService treeService,
            NodeApiMapper mapper,
            PersistenceStrategy persistence,
            TreeViewApiMapper treeViewMapper
    ) {
        this.treeService = treeService;
        this.mapper = mapper;
        this.persistence = persistence;
        this.treeViewMapper = treeViewMapper;
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

        Node parent = persistence.findById(parentId)
                .orElseThrow(() ->
                        new RuntimeException("Parent not found"));

        Node domainNode = mapper.toDomain(request);

        Node computedNode =
                treeService.addChild(
                        parentId,
                        domainNode,
                        parent.getTreeId()
                );

        Node savedNode =
                persistence.save(computedNode);

        return mapper.toResponse(savedNode);
    }
    
 // =====================================================
    // GET TREE
    // =====================================================

    public List<TreeResponse> getTrees() {

        List<Node> roots =
                persistence.findRoots();

        return roots.stream()
                .map(root -> {

                    List<Node> nodes =
                            persistence.findAllByTreeId(
                                    root.getTreeId()
                            );

                    return treeViewMapper.toResponse(
                            treeService.buildTree(nodes)
                    );
                })
                .toList();
    }

    // =====================================================
    // GET SUBTREE
    // =====================================================

    public TreeResponse getSubTree(UUID nodeId) {

        Node node = persistence.findById(nodeId)
                .orElseThrow(() ->
                        new RuntimeException("Node not found"));

        List<Node> nodes =
                persistence.findAllByTreeId(
                        node.getTreeId()
                );

        return treeViewMapper.toResponse(
                treeService.buildSubTree(
                        nodeId,
                        nodes
                )
        );
    }
    
}