package com.grupo2.treeengine.domain;

import java.util.UUID;
/*
 * EL dominio con el que se externalizara la informacion.
 */



public class Node {
    private UUID id;
    private String value;
    private UUID parentId;
    private UUID tree;

    public Node() {}

    public Node(UUID id, String value, UUID parentId) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
        
    }
    
    public Node(UUID id, String value, UUID parentId, UUID tree) {
        this.id = id;
        this.value = value;
        this.parentId = parentId;
        this.tree = tree;
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
    
    public UUID getTreeId() {
    	return tree;
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
}