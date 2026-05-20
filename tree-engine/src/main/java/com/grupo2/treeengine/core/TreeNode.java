package com.grupo2.treeengine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeNode {
	
	//implementacion del nodo del arbol para costume
	private UUID id;
    private String value;
    private List<TreeNode> children = new ArrayList<>();
    private TreeNode parent;
    

    public TreeNode(UUID id, String value, TreeNode parent) {
        this.id = id;
        this.value = value;
        this.parent = parent;
    }
    
    public TreeNode(UUID id, String value) {
        this.id = id;
        this.value = value;
        
    }
    

    public UUID getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }
    
    public TreeNode getParent() {
        return parent;
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }
}
