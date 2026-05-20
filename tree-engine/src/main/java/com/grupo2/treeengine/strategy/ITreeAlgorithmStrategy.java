package com.grupo2.treeengine.strategy;

import java.util.List;
import java.util.UUID;

import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;

public interface ITreeAlgorithmStrategy {
	//Agregando metodos obligatorios de la interfaz. Node  y TreeView son el domain, osea lo externo.
	Node createRoot(Node domainNode);

    Node addChild(UUID parentId, Node domainNode);

    TreeView getTree();

    TreeView getSubTree(UUID nodeId);

    List<Node> getPathToRoot(UUID nodeId);

    List<Node> dfs();

    List<Node> bfs();

    int getHeight();

    int getDepth(UUID nodeId);

    List<Node> getAncestors(UUID nodeId);

    boolean validateNoCycles();
}
