package com.grupo2.treeengine.strategy.impl;

import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.exception.CycleDetectedException;
import com.grupo2.treeengine.exception.NodeNotFoundException;
import com.grupo2.treeengine.exception.RootAlreadyExistsException;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

import java.util.*;

public class CollectionsTreeAlgorithmStrategy implements ITreeAlgorithmStrategy {

    // TreeMap: almacena UUID → Node (ordenado por UUID)
    private final TreeMap<UUID, Node> nodes = new TreeMap<>();

    // TreeMap: almacena parentId → lista de hijos UUID
    private final TreeMap<UUID, List<UUID>> childrenMap = new TreeMap<>();

    // ID de la raíz
    private UUID rootId = null;

    // =====================
    // CREATE ROOT
    // =====================
    @Override
    public Node createRoot(Node domainNode) {
        if (rootId != null) {
            throw new RootAlreadyExistsException();
        }

        UUID id = UUID.randomUUID();
        domainNode.setId(id);
        domainNode.setParentId(null);

        nodes.put(id, domainNode);
        childrenMap.put(id, new ArrayList<>());
        rootId = id;

        return domainNode;
    }

    // =====================
    // ADD CHILD
    // =====================
    @Override
    public Node addChild(UUID parentId, Node domainNode) {
        if (!nodes.containsKey(parentId)) {
            throw new NodeNotFoundException(parentId.toString());
        }

        UUID id = UUID.randomUUID();
        domainNode.setId(id);
        domainNode.setParentId(parentId);

        nodes.put(id, domainNode);
        childrenMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(id);
        childrenMap.put(id, new ArrayList<>());

        return domainNode;
    }

    // =====================
    // GET TREE (completo)
    // =====================
    @Override
    public TreeView getTree() {
        if (rootId == null) return null;
        return buildTreeView(rootId);
    }

    // =====================
    // GET SUBTREE
    // =====================
    @Override
    public TreeView getSubTree(UUID nodeId) {
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId.toString());
        }
        return buildTreeView(nodeId);
    }

    // Construye TreeView recursivo desde un nodo
    private TreeView buildTreeView(UUID nodeId) {
        Node node = nodes.get(nodeId);
        TreeView view = new TreeView(node.getId(), node.getValue());

        for (UUID childId : childrenMap.getOrDefault(nodeId, new ArrayList<>())) {
            view.addChild(buildTreeView(childId));
        }
        return view;
    }

    // =====================
    // GET PATH TO ROOT
    // =====================
    @Override
    public List<Node> getPathToRoot(UUID nodeId) {
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        List<Node> path = new ArrayList<>();
        UUID current = nodeId;

        while (current != null) {
            path.add(nodes.get(current));
            Node currentNode = nodes.get(current);
            current = currentNode.getParentId();
        }
        return path;
    }

    // =====================
    // DFS
    // =====================
    @Override
    public List<Node> dfs() {
        List<Node> result = new ArrayList<>();
        if (rootId == null) return result;

        // ArrayDeque como pila (stack) para DFS
        ArrayDeque<UUID> stack = new ArrayDeque<>();
        stack.push(rootId);

        while (!stack.isEmpty()) {
            UUID nodeId = stack.pop();
            result.add(nodes.get(nodeId));

            List<UUID> children = childrenMap.getOrDefault(nodeId, new ArrayList<>());
            // Invertimos para mantener orden correcto
            for (int i = children.size() - 1; i >= 0; i--) {
                stack.push(children.get(i));
            }
        }
        return result;
    }

    // =====================
    // BFS
    // =====================
    @Override
    public List<Node> bfs() {
        List<Node> result = new ArrayList<>();
        if (rootId == null) return result;

        // ArrayDeque como cola (queue) para BFS
        ArrayDeque<UUID> queue = new ArrayDeque<>();
        queue.add(rootId);

        while (!queue.isEmpty()) {
            UUID nodeId = queue.poll();
            result.add(nodes.get(nodeId));

            List<UUID> children = childrenMap.getOrDefault(nodeId, new ArrayList<>());
            queue.addAll(children);
        }
        return result;
    }

    // =====================
    // GET HEIGHT
    // =====================
    @Override
    public int getHeight() {
        if (rootId == null) return 0;
        return calculateHeight(rootId);
    }

    private int calculateHeight(UUID nodeId) {
        List<UUID> children = childrenMap.getOrDefault(nodeId, new ArrayList<>());
        if (children.isEmpty()) return 1;

        int maxHeight = 0;
        for (UUID childId : children) {
            maxHeight = Math.max(maxHeight, calculateHeight(childId));
        }
        return maxHeight + 1;
    }

    // =====================
    // GET DEPTH
    // =====================
    @Override
    public int getDepth(UUID nodeId) {
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        int depth = 0;
        UUID current = nodeId;

        while (nodes.get(current).getParentId() != null) {
            current = nodes.get(current).getParentId();
            depth++;
        }
        return depth;
    }

    // =====================
    // GET ANCESTORS
    // =====================
    @Override
    public List<Node> getAncestors(UUID nodeId) {
        if (!nodes.containsKey(nodeId)) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        List<Node> ancestors = new ArrayList<>();
        UUID current = nodes.get(nodeId).getParentId();

        while (current != null) {
            ancestors.add(nodes.get(current));
            current = nodes.get(current).getParentId();
        }
        return ancestors;
    }

    // =====================
    // VALIDATE NO CYCLES
    // =====================
    @Override
    public boolean validateNoCycles() {
        Set<UUID> visited = new HashSet<>();
        Set<UUID> inStack = new HashSet<>();

        for (UUID nodeId : nodes.keySet()) {
            if (detectCycle(nodeId, visited, inStack)) {
                throw new CycleDetectedException("Ciclo detectado en nodo: " + nodeId);
            }
        }
        return true;
    }

    private boolean detectCycle(UUID nodeId, Set<UUID> visited, Set<UUID> inStack) {
        if (inStack.contains(nodeId)) return true;
        if (visited.contains(nodeId)) return false;

        visited.add(nodeId);
        inStack.add(nodeId);

        for (UUID childId : childrenMap.getOrDefault(nodeId, new ArrayList<>())) {
            if (detectCycle(childId, visited, inStack)) return true;
        }

        inStack.remove(nodeId);
        return false;
    }
}