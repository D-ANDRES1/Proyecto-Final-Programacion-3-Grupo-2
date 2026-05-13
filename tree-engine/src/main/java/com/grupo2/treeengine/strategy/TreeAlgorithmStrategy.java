package com.grupo2.treeengine.strategy;

import java.util.List;

public interface TreeAlgorithmStrategy {
	//Agregando metodos obligatorios de la interfaz
	String createRoot(String value);

    String addChild(String parentId, String value);

    List<String> getTree();

    List<String> getSubTree(String nodeId);

    List<String> getPathToRoot(String nodeId);

    List<String> dfs();

    List<String> bfs();

    int getHeight();

    int getDepth(String nodeId);

    List<String> getAncestors(String nodeId);

    boolean validateNoCycles();
}
