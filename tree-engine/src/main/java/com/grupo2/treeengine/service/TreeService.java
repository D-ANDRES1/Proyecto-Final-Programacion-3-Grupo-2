package com.grupo2.treeengine.service;

import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

import java.util.List;
import java.util.UUID;

//Service para delegar, no calcula nada
public class TreeService {

    private final ITreeAlgorithmStrategy strategy;
    
    //Indica que estrategia se usara en el service
    public TreeService(ITreeAlgorithmStrategy strategy) {
        this.strategy = strategy;
    }

    public Node createRoot(Node domainNode) {
        return strategy.createRoot(domainNode);
    }

    public Node addChild(UUID parentId, Node domainNode) {
        return strategy.addChild(parentId, domainNode);
    }

    public TreeView getTree() {
        return strategy.getTree();
    }

    public TreeView getSubTree(UUID nodeId) {
        return strategy.getSubTree(nodeId);
    }

    public List<Node> getPathToRoot(UUID nodeId) {
        return strategy.getPathToRoot(nodeId);
    }

    public List<Node> dfs() {
        return strategy.dfs();
    }

    public List<Node> bfs() {
        return strategy.bfs();
    }

    public int getHeight() {
        return strategy.getHeight();
    }

    public int getDepth(UUID nodeId) {
        return strategy.getDepth(nodeId);
    }

    public List<Node> getAncestors(UUID nodeId) {
        return strategy.getAncestors(nodeId);
    }

    public boolean validateNoCycles() {
        return strategy.validateNoCycles();
    }
}