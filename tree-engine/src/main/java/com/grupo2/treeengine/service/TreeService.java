package com.grupo2.treeengine.service;

import com.grupo2.treeengine.strategy.TreeAlgorithmStrategy;

import java.util.List;

//Service para delegar, no calcula nada
public class TreeService {

    private final TreeAlgorithmStrategy strategy;
    
    //Indica que estrategia se usara en el service
    public TreeService(TreeAlgorithmStrategy strategy) {
        this.strategy = strategy;
    }

    public String createRoot(String value) {
        return strategy.createRoot(value);
    }

    public String addChild(String parentId, String value) {
        return strategy.addChild(parentId, value);
    }

    public List<String> getTree() {
        return strategy.getTree();
    }

    public List<String> getSubTree(String nodeId) {
        return strategy.getSubTree(nodeId);
    }

    public List<String> getPathToRoot(String nodeId) {
        return strategy.getPathToRoot(nodeId);
    }

    public List<String> dfs() {
        return strategy.dfs();
    }

    public List<String> bfs() {
        return strategy.bfs();
    }

    public int getHeight() {
        return strategy.getHeight();
    }

    public int getDepth(String nodeId) {
        return strategy.getDepth(nodeId);
    }

    public List<String> getAncestors(String nodeId) {
        return strategy.getAncestors(nodeId);
    }

    public boolean validateNoCycles() {
        return strategy.validateNoCycles();
    }
}