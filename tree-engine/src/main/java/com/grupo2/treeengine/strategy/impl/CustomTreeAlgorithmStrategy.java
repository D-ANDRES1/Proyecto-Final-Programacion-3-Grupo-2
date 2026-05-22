package com.grupo2.treeengine.strategy.impl;

import java.util.*;

import com.grupo2.treeengine.core.TreeNode;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

public class CustomTreeAlgorithmStrategy
        implements ITreeAlgorithmStrategy {

    // =====================================================
    // CREATE OPERATIONS
    // =====================================================

    @Override
    public Node createRoot(Node node) {

        UUID nodeId = UUID.randomUUID();

        UUID treeId = UUID.randomUUID();

        node.setId(nodeId);

        node.setTreeId(treeId);

        node.setParentId(null);

        return node;
    }

    @Override
    public Node addChild(
            UUID parentId,
            Node node,
            UUID treeId
    ) {

        node.setId(UUID.randomUUID());

        node.setParentId(parentId);

        node.setTreeId(treeId);

        return node;
    }

    // =====================================================
    // TREE BUILDING
    // =====================================================

    @Override
    public TreeView buildTree(List<Node> nodes) {

        TreeNode root = buildInternalTree(nodes);

        if (root == null) {
            return null;
        }

        return toTreeView(root);
    }

    @Override
    public TreeView buildSubTree(UUID nodeId, List<Node> nodes) {

        // ✅ buildInternalTree arma todas las relaciones padre-hijo
        Map<UUID, TreeNode> map = buildNodeMap(nodes);

        // ✅ asignar hijos igual que buildInternalTree
        for (Node node : nodes) {
            if (node.getParentId() != null) {
                TreeNode parent = map.get(node.getParentId());
                TreeNode child = map.get(node.getId());
                if (parent != null && child != null) {
                    parent.addChild(child);
                }
            }
        }

        TreeNode node = map.get(nodeId);
        if (node == null) return null;

        return toTreeView(node);
    }

    // =====================================================
    // DFS
    // =====================================================

    @Override
    public List<Node> dfs(List<Node> nodes) {

        List<Node> result =
                new ArrayList<>();

        TreeNode root =
                buildInternalTree(nodes);

        if (root == null) {
            return result;
        }

        dfsRecursive(root, result);

        return result;
    }

    private void dfsRecursive(
            TreeNode node,
            List<Node> result
    ) {

        result.add(
                new Node(
                        node.getId(),
                        node.getValue(),
                        node.getParentId(),
                        null
                )
        );

        for (TreeNode child : node.getChildren()) {
            dfsRecursive(child, result);
        }
    }

    // =====================================================
    // BFS
    // =====================================================

    @Override
    public List<Node> bfs(List<Node> nodes) {

        List<Node> result =
                new ArrayList<>();

        TreeNode root =
                buildInternalTree(nodes);

        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue =
                new LinkedList<>();

        queue.add(root);

        while (!queue.isEmpty()) {

            TreeNode current =
                    queue.poll();

            result.add(
                    new Node(
                            current.getId(),
                            current.getValue(),
                            current.getParentId(),
                            null
                    )
            );

            queue.addAll(current.getChildren());
        }

        return result;
    }

    // =====================================================
    // PATH TO ROOT
    // =====================================================

    @Override
    public List<Node> getPathToRoot(
            UUID nodeId,
            List<Node> nodes
    ) {

        Map<UUID, Node> map =
                nodeMap(nodes);

        List<Node> path =
                new ArrayList<>();

        Node current = map.get(nodeId);

        while (current != null) {

            path.add(0, current);

            current =
                    map.get(current.getParentId());
        }

        return path;
    }

    // =====================================================
    // ANCESTORS
    // =====================================================

    @Override
    public List<Node> getAncestors(
            UUID nodeId,
            List<Node> nodes
    ) {

        List<Node> path =
                getPathToRoot(nodeId, nodes);

        if (!path.isEmpty()) {
            path.remove(path.size() - 1);
        }

        return path;
    }

    // =====================================================
    // HEIGHT
    // =====================================================

    @Override
    public int getHeight(List<Node> nodes) {

        TreeNode root =
                buildInternalTree(nodes);

        return height(root);
    }

    private int height(TreeNode node) {

        if (node == null) {
            return 0;
        }

        int max = 0;

        for (TreeNode child : node.getChildren()) {

            max =
                    Math.max(
                            max,
                            height(child)
                    );
        }

        return max + 1;
    }

    // =====================================================
    // DEPTH
    // =====================================================

    @Override
    public int getDepth(
            UUID nodeId,
            List<Node> nodes
    ) {

        return getAncestors(
                nodeId,
                nodes
        ).size();
    }

    // =====================================================
    // VALIDATE CYCLES
    // =====================================================

    @Override
    public boolean validateNoCycles(
            List<Node> nodes
    ) {

        Map<UUID, Node> map =
                nodeMap(nodes);

        for (Node node : nodes) {

            Set<UUID> visited =
                    new HashSet<>();

            Node current = node;

            while (current != null) {

                if (
                    visited.contains(
                            current.getId()
                    )
                ) {
                    return false;
                }

                visited.add(current.getId());

                current =
                        map.get(
                                current.getParentId()
                        );
            }
        }

        return true;
    }

    // =====================================================
    // INTERNAL BUILDERS
    // =====================================================

    private TreeNode buildInternalTree(
            List<Node> nodes
    ) {

        Map<UUID, TreeNode> map =
                buildNodeMap(nodes);

        TreeNode root = null;

        for (Node node : nodes) {

            TreeNode current =
                    map.get(node.getId());

            if (node.getParentId() == null) {

                root = current;

            } else {

                TreeNode parent =
                        map.get(
                                node.getParentId()
                        );

                if (parent != null) {
                    parent.addChild(current);
                }
            }
        }

        return root;
    }

    private Map<UUID, TreeNode> buildNodeMap(
            List<Node> nodes
    ) {

        Map<UUID, TreeNode> map =
                new HashMap<>();

        for (Node node : nodes) {

            map.put(
                    node.getId(),
                    new TreeNode(
                            node.getId(),
                            node.getValue(),
                            node.getParentId()
                    )
            );
        }

        return map;
    }

    private Map<UUID, Node> nodeMap(
            List<Node> nodes
    ) {

        Map<UUID, Node> map =
                new HashMap<>();

        for (Node node : nodes) {
            map.put(node.getId(), node);
        }

        return map;
    }

    // =====================================================
    // TREE VIEW
    // =====================================================

    private TreeView toTreeView(
            TreeNode node
    ) {

        TreeView view =
                new TreeView(
                        node.getId(),
                        node.getValue()
                );

        for (TreeNode child : node.getChildren()) {
            view.addChild(
                    toTreeView(child)
            );
        }

        return view;
    }
}