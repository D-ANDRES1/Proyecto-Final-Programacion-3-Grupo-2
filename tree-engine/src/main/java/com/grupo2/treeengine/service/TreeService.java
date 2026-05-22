package com.grupo2.treeengine.service;

import java.util.List;
import java.util.UUID;

import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

/*
 * Service del engine.
 * SOLO delega algoritmos.
 * NO guarda estado.
 */
public class TreeService {

    private final ITreeAlgorithmStrategy strategy;

    public TreeService(
            ITreeAlgorithmStrategy strategy
    ) {
        this.strategy = strategy;
    }

    // =====================================================
    // CREATE OPERATIONS
    // =====================================================

    public Node createRoot(Node node) {

        return strategy.createRoot(node);
    }

    public Node addChild(
            UUID parentId,
            Node node,
            UUID treeId
    ) {

        return strategy.addChild(
                parentId,
                node,
                treeId
        );
    }

    // =====================================================
    // TREE BUILDING
    // =====================================================

    public TreeView buildTree(
            List<Node> nodes
    ) {

        return strategy.buildTree(nodes);
    }

    public TreeView buildSubTree(
            UUID nodeId,
            List<Node> nodes
    ) {

        return strategy.buildSubTree(
                nodeId,
                nodes
        );
    }

    // =====================================================
    // TRAVERSALS
    // =====================================================

    public List<Node> dfs(
            List<Node> nodes
    ) {

        return strategy.dfs(nodes);
    }

    public List<Node> bfs(
            List<Node> nodes
    ) {

        return strategy.bfs(nodes);
    }

    // =====================================================
    // QUERIES
    // =====================================================

    public List<Node> getPathToRoot(
            UUID nodeId,
            List<Node> nodes
    ) {

        return strategy.getPathToRoot(
                nodeId,
                nodes
        );
    }

    public List<Node> getAncestors(
            UUID nodeId,
            List<Node> nodes
    ) {

        return strategy.getAncestors(
                nodeId,
                nodes
        );
    }

    public int getHeight(
            List<Node> nodes
    ) {

        return strategy.getHeight(nodes);
    }

    public int getDepth(
            UUID nodeId,
            List<Node> nodes
    ) {

        return strategy.getDepth(
                nodeId,
                nodes
        );
    }

    public boolean validateNoCycles(
            List<Node> nodes
    ) {

        return strategy.validateNoCycles(nodes);
    }
}