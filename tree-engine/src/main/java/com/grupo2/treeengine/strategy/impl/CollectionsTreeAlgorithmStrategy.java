package com.grupo2.treeengine.strategy.impl;

import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;
import com.grupo2.treeengine.exception.CycleDetectedException;
import com.grupo2.treeengine.exception.NodeNotFoundException;
import com.grupo2.treeengine.exception.RootAlreadyExistsException;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;

import java.util.*;

public class CollectionsTreeAlgorithmStrategy implements ITreeAlgorithmStrategy {

    /**
     * NO hay estado interno.
     * Todos los métodos reciben List<Node> como parámetro.
     */

    // =====================================================
    // CREATE OPERATIONS
    // =====================================================

    @Override
    public Node createRoot(Node node) {
        // Solo genera UUID si no lo tiene
        if (node.getId() == null) {
            node.setId(UUID.randomUUID());
        }
        if (node.getTreeId() == null) {  
            node.setTreeId(UUID.randomUUID());
        }
        node.setParentId(null);
        return node;
    }

    @Override
    public Node addChild(UUID parentId, Node node, UUID treeId) {
        // Solo genera UUID y setea parentId
        if (node.getId() == null) {
            node.setId(UUID.randomUUID());
        }
        node.setParentId(parentId);
        node.setTreeId(treeId);
        return node;
    }

    // =====================================================
    // TREE BUILDING - Construye TreeView desde List<Node>
    // =====================================================

    @Override
    public TreeView buildTree(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }

        // Encuentra la raíz (el nodo sin padre)
        Node rootNode = nodes.stream()
                .filter(n -> n.getParentId() == null)
                .findFirst()
                .orElse(null);

        if (rootNode == null) {
            return null;
        }

        return buildTreeViewRecursive(rootNode, nodes);
    }

    @Override
    public TreeView buildSubTree(UUID nodeId, List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        // Encuentra el nodo raíz del subarbol
        Node subRootNode = nodes.stream()
                .filter(n -> n.getId().equals(nodeId))
                .findFirst()
                .orElseThrow(() -> 
                        new NodeNotFoundException(nodeId.toString()));

        return buildTreeViewRecursive(subRootNode, nodes);
    }

    /**
     * Construye TreeView recursivamente desde un nodo.
     * Busca los hijos en la lista de nodos.
     */
    private TreeView buildTreeViewRecursive(Node node, List<Node> allNodes) {
        TreeView view = new TreeView(node.getId(), node.getValue());

        // Encuentra todos los hijos de este nodo
        List<Node> children = allNodes.stream()
                .filter(n -> node.getId().equals(n.getParentId()))
                .toList();

        // Construye recursivamente los hijos
        for (Node child : children) {
            view.addChild(buildTreeViewRecursive(child, allNodes));
        }

        return view;
    }

    // =====================================================
    // TRAVERSALS - Usa List<Node> directamente
    // =====================================================

    @Override
    public List<Node> dfs(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Node> result = new ArrayList<>();
        
        // Encuentra la raíz
        Node root = nodes.stream()
                .filter(n -> n.getParentId() == null)
                .findFirst()
                .orElse(null);

        if (root == null) {
            return new ArrayList<>();
        }

        // DFS con pila (ArrayDeque)
        ArrayDeque<UUID> stack = new ArrayDeque<>();
        stack.push(root.getId());

        while (!stack.isEmpty()) {
            UUID currentId = stack.pop();
            Node currentNode = findNodeById(currentId, nodes);

            if (currentNode != null) {
                result.add(currentNode);

                // Agrega hijos en orden inverso (para mantener orden)
                List<Node> children = nodes.stream()
                        .filter(n -> currentId.equals(n.getParentId()))
                        .sorted(Comparator.comparing(Node::getId).reversed())
                        .toList();

                for (Node child : children) {
                    stack.push(child.getId());
                }
            }
        }

        return result;
    }

    @Override
    public List<Node> bfs(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Node> result = new ArrayList<>();
        
        // Encuentra la raíz
        Node root = nodes.stream()
                .filter(n -> n.getParentId() == null)
                .findFirst()
                .orElse(null);

        if (root == null) {
            return new ArrayList<>();
        }

        // BFS con cola (ArrayDeque)
        ArrayDeque<UUID> queue = new ArrayDeque<>();
        queue.add(root.getId());

        while (!queue.isEmpty()) {
            UUID currentId = queue.poll();
            Node currentNode = findNodeById(currentId, nodes);

            if (currentNode != null) {
                result.add(currentNode);

                // Agrega hijos a la cola
                List<Node> children = nodes.stream()
                        .filter(n -> currentId.equals(n.getParentId()))
                        .toList();

                for (Node child : children) {
                    queue.add(child.getId());
                }
            }
        }

        return result;
    }

    // =====================================================
    // QUERIES - Reciben List<Node> como parámetro
    // =====================================================

    @Override
    public List<Node> getPathToRoot(UUID nodeId, List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        List<Node> path = new ArrayList<>();
        Node current = findNodeById(nodeId, nodes);

        if (current == null) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        // Sube hasta la raíz
        while (current != null) {
            path.add(current);
            current = current.getParentId() != null
                    ? findNodeById(current.getParentId(), nodes)
                    : null;
        }

        return path;
    }

    @Override
    public List<Node> getAncestors(UUID nodeId, List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        List<Node> ancestors = new ArrayList<>();
        Node current = findNodeById(nodeId, nodes);

        if (current == null) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        // Sube por los padres (sin incluir el nodo mismo)
        UUID parentId = current.getParentId();
        while (parentId != null) {
            Node parent = findNodeById(parentId, nodes);
            if (parent != null) {
                ancestors.add(parent);
                parentId = parent.getParentId();
            } else {
                break;
            }
        }

        return ancestors;
    }

    @Override
    public int getHeight(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return 0;
        }

        Node root = nodes.stream()
                .filter(n -> n.getParentId() == null)
                .findFirst()
                .orElse(null);

        if (root == null) {
            return 0;
        }

        return calculateHeight(root, nodes);
    }

    private int calculateHeight(Node node, List<Node> allNodes) {
        List<Node> children = allNodes.stream()
                .filter(n -> node.getId().equals(n.getParentId()))
                .toList();

        if (children.isEmpty()) {
            return 1;
        }

        int maxHeight = 0;
        for (Node child : children) {
            maxHeight = Math.max(maxHeight, calculateHeight(child, allNodes));
        }
        return maxHeight + 1;
    }

    @Override
    public int getDepth(UUID nodeId, List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        Node node = findNodeById(nodeId, nodes);
        if (node == null) {
            throw new NodeNotFoundException(nodeId.toString());
        }

        int depth = 0;
        UUID currentId = node.getParentId();

        while (currentId != null) {
            depth++;
            Node parent = findNodeById(currentId, nodes);
            if (parent != null) {
                currentId = parent.getParentId();
            } else {
                break;
            }
        }

        return depth;
    }

    @Override
    public boolean validateNoCycles(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return true;
        }

        Set<UUID> visited = new HashSet<>();
        Set<UUID> inStack = new HashSet<>();

        for (Node node : nodes) {
            if (detectCycle(node.getId(), nodes, visited, inStack)) {
                throw new CycleDetectedException(
                        "Ciclo detectado en nodo: " + node.getId());
            }
        }
        return true;
    }

    private boolean detectCycle(UUID nodeId, List<Node> allNodes,
                                Set<UUID> visited, Set<UUID> inStack) {
        if (inStack.contains(nodeId)) {
            return true;
        }
        if (visited.contains(nodeId)) {
            return false;
        }

        visited.add(nodeId);
        inStack.add(nodeId);

        // Busca los hijos
        List<Node> children = allNodes.stream()
                .filter(n -> nodeId.equals(n.getParentId()))
                .toList();

        for (Node child : children) {
            if (detectCycle(child.getId(), allNodes, visited, inStack)) {
                return true;
            }
        }

        inStack.remove(nodeId);
        return false;
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private Node findNodeById(UUID id, List<Node> nodes) {
        return nodes.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}