package com.grupo2.treeengine.strategy.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.grupo2.treeengine.core.TreeNode;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

public class CustomTreeAlgorithmStrategy implements ITreeAlgorithmStrategy {
	
	private TreeNode root; // ✅ el árbol en memoria

    @Override
    public Node createRoot(Node value) {
        // La estrategia solo define lógica de árbol, no genera treeId
        root = new TreeNode(null, value.getValue(), null);

        return new Node(
            null,             // id: lo genera la BD
            value.getValue(),
            null,             // parentId: es raíz
            null              // treeId: lo asigna JpaPersistenceStrategy
        );
    }

    @Override
    public Node addChild(UUID parentId, Node value) {
        // TODO: buscar el padre en el árbol en memoria y agregar hijo
        return new Node(
            null,
            value.getValue(),
            parentId,
            null  // treeId: lo asigna JpaPersistenceStrategy del padre
        );
    }

	@Override
	public TreeView getTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeView getSubTree(UUID nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> getPathToRoot(UUID nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> dfs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Node> bfs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDepth(UUID nodeId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Node> getAncestors(UUID nodeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateNoCycles() {
		// TODO Auto-generated method stub
		return false;
	}

}
