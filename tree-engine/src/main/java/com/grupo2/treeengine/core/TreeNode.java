package com.grupo2.treeengine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeNode {

    private UUID id;

    private String value;

    private UUID parentId;

    private final List<TreeNode> children = new ArrayList<>();

    public TreeNode(
            UUID id,
            String value,
            UUID parentId
    ) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
    }

    public UUID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public UUID getParentId() {
        return parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}