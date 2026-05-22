package com.grupo2.treeengine.strategy;

import java.util.List;
import java.util.UUID;

import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.domain.TreeView;

public interface ITreeAlgorithmStrategy {

    // CREATE OPERATIONS

    Node createRoot(Node node);

    Node addChild(
            UUID parentId,
            Node node,
            UUID treeId
    );

    // TREE BUILDING

    TreeView buildTree(List<Node> nodes);

    TreeView buildSubTree(
            UUID nodeId,
            List<Node> nodes
    );

    // TRAVERSALS

    List<Node> dfs(List<Node> nodes);

    List<Node> bfs(List<Node> nodes);

    // QUERIES

    List<Node> getPathToRoot(
            UUID nodeId,
            List<Node> nodes
    );

    List<Node> getAncestors(
            UUID nodeId,
            List<Node> nodes
    );

    int getHeight(List<Node> nodes);

    int getDepth(
            UUID nodeId,
            List<Node> nodes
    );

    boolean validateNoCycles(List<Node> nodes);
}