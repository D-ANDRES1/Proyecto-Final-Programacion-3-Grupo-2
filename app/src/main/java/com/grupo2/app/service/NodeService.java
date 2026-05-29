package com.grupo2.app.service;

import com.grupo2.app.mapper.api.NodeApiMapper;
import com.grupo2.app.mapper.api.TreeViewApiMapper;
import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.DepthResponse;
import com.grupo2.app.model.NodeListResponse;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.model.TreeInfoResponse;
import com.grupo2.app.model.TreeResponse;
import com.grupo2.app.model.UpdateTreeRequest;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.service.TreeService;
import com.grupo2.app.model.HeightResponse;
import com.grupo2.app.model.ValidationResponse;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

 // =====================================================
 // GET DEPTH
 // =====================================================

 public DepthResponse getDepth(UUID nodeId) {

     Node node = persistence.findById(nodeId)
             .orElseThrow(() ->
                     new RuntimeException("Node not found"));

     List<Node> allNodes =
             persistence.findAllByTreeId(node.getTreeId());

     int depth = treeService.getDepth(nodeId, allNodes);

     DepthResponse response = new DepthResponse();
     response.setDepth(depth);

     return response;
 }

 // =====================================================
 // GET PATH TO ROOT
 // =====================================================

 public NodeListResponse getPathToRoot(UUID nodeId) {

     Node node = persistence.findById(nodeId)
             .orElseThrow(() ->
                     new RuntimeException("Node not found"));

     List<Node> allNodes =
             persistence.findAllByTreeId(node.getTreeId());

     List<Node> path =
             treeService.getPathToRoot(nodeId, allNodes);

     NodeListResponse response = new NodeListResponse();

     response.setItems(
             path.stream()
                     .map(mapper::toResponse)
                     .toList()
     );

     return response;
 }
    
    
 // =====================================================
 // GET ANCESTORS
 // =====================================================

 public NodeListResponse getAncestors(UUID nodeId) {

     Node node = persistence.findById(nodeId)
             .orElseThrow(() ->
                     new RuntimeException("Node not found"));

     List<Node> allNodes =
             persistence.findAllByTreeId(node.getTreeId());

     List<Node> ancestors =
             treeService.getAncestors(nodeId, allNodes);

     NodeListResponse response = new NodeListResponse();

     response.setItems(
             ancestors.stream()
                     .map(mapper::toResponse)
                     .toList()
     );

     return response;
 }
 
//=====================================================
//GET HEIGHT
//=====================================================

public HeightResponse getHeight(UUID treeId) {

  List<Node> allNodes = persistence.findAllByTreeId(treeId);

  int height = treeService.getHeight(allNodes);

  HeightResponse response = new HeightResponse();
  response.setHeight(height);

  return response;
}

//=====================================================
//VALIDATE NO CYCLES
//=====================================================

public ValidationResponse validateNoCycles(UUID treeId) {

  List<Node> allNodes = persistence.findAllByTreeId(treeId);

  boolean valid = treeService.validateNoCycles(allNodes);

  ValidationResponse response = new ValidationResponse();
  response.setValid(valid);

  return response;
}
 
 public NodeListResponse traversal(String type) {

	    // ✅ cargar todos los nodos que sí tienen treeId
	    List<Node> allNodes = persistence.findAll();

	    // ✅ obtener treeIds únicos
	    List<UUID> treeIds = allNodes.stream()
	            .map(Node::getTreeId)
	            .filter(id -> id != null)
	            .distinct()
	            .toList();

	    List<Node> result = new ArrayList<>();

	    for (UUID treeId : treeIds) {

	        List<Node> treeNodes = allNodes.stream()
	                .filter(n -> treeId.equals(n.getTreeId()))
	                .toList();

	        List<Node> traversed = type.equals("DFS")
	                ? treeService.dfs(treeNodes)
	                : treeService.bfs(treeNodes);

	        result.addAll(traversed);
	    }

	    NodeListResponse response = new NodeListResponse();
	    response.setItems(
	            result.stream()
	                    .map(mapper::toResponse)
	                    .toList()
	    );

	    return response;
	}
 
//=====================================================
//UPDATE TREE
//=====================================================

 public TreeInfoResponse updateTree(UUID treeId, UpdateTreeRequest request) {

	    persistence.updateTree(treeId, request.getComputerName(), request.getManufacturer());

	    TreeInfoResponse response = new TreeInfoResponse();
	    response.setId(treeId);
	    response.setComputerName(request.getComputerName());
	    response.setManufacturer(request.getManufacturer());

	    return response;
	}
    
}