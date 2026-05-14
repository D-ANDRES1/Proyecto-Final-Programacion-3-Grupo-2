package com.grupo2.treeengine.strategy.impl;

import com.grupo2.treeengine.strategy.TreeAlgorithmStrategy;
import java.util.*;

public class CollectionsTreeAlgorithmStrategy implements TreeAlgorithmStrategy {

    // TreeMap: almacenará nodos por ID (implementación semana 2)
    private final TreeMap<String, Object> nodes = new TreeMap<>();

    // TreeMap: guardará relación padre-hijo (implementación semana 2)
    private final TreeMap<String, String> parentMap = new TreeMap<>();

    @Override
    public String createRoot(String value) {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public String addChild(String parentId, String value) {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public List<String> getTree() {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public List<String> getSubTree(String nodeId) {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public List<String> getPathToRoot(String nodeId) {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public List<String> dfs() {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public List<String> bfs() {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public int getDepth(String nodeId) {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public List<String> getAncestors(String nodeId) {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }

    @Override
    public boolean validateNoCycles() {
        throw new UnsupportedOperationException("Pendiente semana 2");
    }
}