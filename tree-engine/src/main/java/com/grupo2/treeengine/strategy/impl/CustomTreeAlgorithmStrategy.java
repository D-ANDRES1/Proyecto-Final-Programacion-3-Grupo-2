package com.grupo2.treeengine.strategy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import com.grupo2.treeengine.core.TreeNode;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

public class CustomTreeAlgorithmStrategy implements ITreeAlgorithmStrategy {
	
	private TreeNode root; // ✅ el árbol en memoria
	private final Map<UUID, TreeNode> nodeMap = new HashMap<>();

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
        if (root == null) return null;
        return toTreeView(root);
    }

    @Override
    public TreeView getSubTree(UUID nodeId) {
        TreeNode node = nodeMap.get(nodeId);
        if (node == null) return null;
        return toTreeView(node);
    }

	@Override
    public List<Node> getPathToRoot(UUID nodeId) {
        TreeNode node = nodeMap.get(nodeId);
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(0, toNode(node)); // insertar al inicio → orden raíz primero
            node = node.getParent();
        }
        return path;
    }

	@Override
    public List<Node> dfs() {
        List<Node> result = new ArrayList<>();
        if (root != null) dfsHelper(root, result);
        return result;
    }
	
	private void dfsHelper(TreeNode node, List<Node> result) {
        result.add(toNode(node));
        for (TreeNode child : node.getChildren()) {
            dfsHelper(child, result);
        }
    }
	
	@Override
    public List<Node> bfs() {
        List<Node> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            result.add(toNode(current));
            queue.addAll(current.getChildren());
        }

        return result;
	}
    

	@Override
    public int getHeight() {
        return heightHelper(root);
    }
	
	private int heightHelper(TreeNode node) {
        if (node == null || node.getChildren().isEmpty()) return 0;
        int max = 0;
        for (TreeNode child : node.getChildren()) {
            max = Math.max(max, heightHelper(child));
        }
        return max + 1;
    }

	@Override
    public int getDepth(UUID nodeId) {
        TreeNode node = nodeMap.get(nodeId);
        int depth = 0;
        while (node != null && node.getParent() != null) {
            depth++;
            node = node.getParent();
        }
        return depth;
    }

	@Override
    public List<Node> getAncestors(UUID nodeId) {
        TreeNode node = nodeMap.get(nodeId);
        List<Node> ancestors = new ArrayList<>();
        if (node == null) return ancestors;

        node = node.getParent();
        while (node != null) {
            ancestors.add(toNode(node));
            node = node.getParent();
        }
        return ancestors;
    }
	@Override
    public boolean validateNoCycles() {
        if (root == null) return true;
        Set<UUID> visited = new HashSet<>();
        return validateHelper(root, visited);
    }
	
	private boolean validateHelper(TreeNode node, Set<UUID> visited) {
        if (node.getId() != null) {
            if (visited.contains(node.getId())) return false;
            visited.add(node.getId());
        }
        for (TreeNode child : node.getChildren()) {
            if (!validateHelper(child, visited)) return false;
        }
        return true;
    }
	
	private TreeView toTreeView(TreeNode node) {
        TreeView view = new TreeView(node.getId(), node.getValue());
        for (TreeNode child : node.getChildren()) {
            view.addChild(toTreeView(child));
        }
        return view;
    }

    private Node toNode(TreeNode node) {
        UUID parentId = node.getParent() != null ? node.getParent().getId() : null;
        return new Node(node.getId(), node.getValue(), parentId);
    }

}
