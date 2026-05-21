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

/**
 * Estrategia de algoritmos en memoria. Java puro (sin Spring).
 */
public class CustomTreeAlgorithmStrategy implements ITreeAlgorithmStrategy {
	
	private TreeNode root;
	private final Map<UUID, TreeNode> nodeMap = new HashMap<>();

    @Override
    public Node createRoot(Node value) {
        UUID id = value.getId();
        root = new TreeNode(id, value.getValue(), null);
        nodeMap.clear();
        nodeMap.put(id, root);
        return new Node(id, value.getValue(), null, null);
    }

    @Override
    public Node addChild(UUID parentId, Node value) {
        TreeNode parent = nodeMap.get(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found in memory: " + parentId);
        }
        UUID childId = value.getId();
        TreeNode child = new TreeNode(childId, value.getValue(), parent);
        parent.getChildren().add(child);
        nodeMap.put(childId, child);
        return new Node(childId, value.getValue(), parentId, null);
    }

    @Override public TreeView getTree() {
        return root == null ? null : toTreeView(root);
    }
    @Override public TreeView getSubTree(UUID nodeId) {
        TreeNode node = nodeMap.get(nodeId);
        return node == null ? null : toTreeView(node);
    }
    @Override public List<Node> getPathToRoot(UUID nodeId) {
        TreeNode node = nodeMap.get(nodeId);
        List<Node> path = new ArrayList<>();
        while (node != null) { path.add(0, toNode(node)); node = node.getParent(); }
        return path;
    }
    @Override public List<Node> dfs() {
        List<Node> res = new ArrayList<>();
        if (root != null) dfsHelper(root, res);
        return res;
    }
    private void dfsHelper(TreeNode n, List<Node> res) {
        res.add(toNode(n));
        for (TreeNode c : n.getChildren()) dfsHelper(c, res);
    }
    @Override public List<Node> bfs() {
        List<Node> res = new ArrayList<>();
        if (root == null) return res;
        Queue<TreeNode> q = new LinkedList<>(); q.add(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll(); res.add(toNode(cur)); q.addAll(cur.getChildren());
        }
        return res;
    }
    @Override public int getHeight() { return heightHelper(root); }
    private int heightHelper(TreeNode n) {
        if (n == null || n.getChildren().isEmpty()) return 0;
        int max = 0;
        for (TreeNode c : n.getChildren()) max = Math.max(max, heightHelper(c));
        return max + 1;
    }
    @Override public int getDepth(UUID nodeId) {
        TreeNode n = nodeMap.get(nodeId); int d = 0;
        while (n != null && n.getParent() != null) { d++; n = n.getParent(); }
        return d;
    }
    @Override public List<Node> getAncestors(UUID nodeId) {
        TreeNode n = nodeMap.get(nodeId); List<Node> a = new ArrayList<>();
        if (n == null) return a;
        n = n.getParent();
        while (n != null) { a.add(toNode(n)); n = n.getParent(); }
        return a;
    }
    @Override public boolean validateNoCycles() {
        if (root == null) return true;
        Set<UUID> v = new HashSet<>(); return validateHelper(root, v);
    }
    private boolean validateHelper(TreeNode n, Set<UUID> v) {
        if (n.getId() != null) { if (v.contains(n.getId())) return false; v.add(n.getId()); }
        for (TreeNode c : n.getChildren()) if (!validateHelper(c, v)) return false;
        return true;
    }

    private TreeView toTreeView(TreeNode n) {
        TreeView v = new TreeView(n.getId(), n.getValue());
        for (TreeNode c : n.getChildren()) v.addChild(toTreeView(c));
        return v;
    }
    private Node toNode(TreeNode n) {
        return new Node(n.getId(), n.getValue(), n.getParent() != null ? n.getParent().getId() : null);
    }

    /**
     * HIDRATACIÓN: Reconstruye el árbol en memoria desde una lista de nodos.
     * Llamado por Spring (TreeInitializer) al iniciar la app.
     */
    public void loadFromDatabase(List<Node> allNodes) {
        nodeMap.clear(); root = null;
        for (Node d : allNodes) {
            if (d.getId() != null) nodeMap.put(d.getId(), new TreeNode(d.getId(), d.getValue(), null));
        }
        for (Node d : allNodes) {
            TreeNode n = nodeMap.get(d.getId()); if (n == null) continue;
            if (d.getParentId() == null) root = n;
            else {
                TreeNode p = nodeMap.get(d.getParentId());
                if (p != null) { n.setParent(p); p.getChildren().add(n); }
            }
        }
    }
}