package com.grupo2.treeengine.core;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	
	//implementacion del nodo del arbol para costume o tambien utlizable en collection 
	private String id;
    private String value;
    private List<TreeNode> children = new ArrayList<>();

    public TreeNode(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        this.children.add(child);
    }
}
