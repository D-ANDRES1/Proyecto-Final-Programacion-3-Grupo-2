package com.grupo2.treeengine.domain;

import java.util.UUID;

public class Node {

    private UUID id;

    private String value;

    private UUID parentId;

    private UUID treeId;

    public Node() {}

    public Node(
            UUID id,
            String value,
            UUID parentId,
            UUID treeId
    ) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
        this.treeId = treeId;
    }

    public Node(String value) {
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public UUID getTreeId() {
        return treeId;
    }

    public void setTreeId(UUID treeId) {
        this.treeId = treeId;
    }
}