package com.grupo2.treeengine.domain;

/*
 * DOminio para operacionces jerarquicas
 */
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeView {

    private UUID id;

    private String value;

    private List<TreeView> children = new ArrayList<>();

    public TreeView() {
    }

    public TreeView(UUID id, String value) {
        this.id = id;
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

    public List<TreeView> getChildren() {
        return children;
    }

    public void setChildren(List<TreeView> children) {
        this.children = children;
    }

    public void addChild(TreeView child) {
        this.children.add(child);
    }
}