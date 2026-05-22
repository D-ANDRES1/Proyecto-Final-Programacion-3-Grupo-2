package com.grupo2.app.model;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "nodes")
public class NodeEntity {
    
    @Id
    private UUID id;

    @Column(name = "node_value", length = 255, nullable = false)
    @NotBlank(message = "El valor del nodo no puede estar vacío")
    @Size(min = 1, max = 64, message = "El valor debe tener entre 1 y 64 caracteres")
    private String value;

    @Column(name = "parent_id", length = 36)
    private UUID parentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tree_id")
    private TreeEntity tree;

    @Transient
    private java.util.List<NodeEntity> children = new java.util.ArrayList<>();

    // Getters y Setters
    public UUID getId() {
        return id;
    }
    
    public TreeEntity getTree() {
    	return tree;
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
    
    public void setTree(TreeEntity tree) { // ✅
        this.tree = tree;
    }

    public java.util.List<NodeEntity> getChildren() {
        return children;
    }

    public void setChildren(java.util.List<NodeEntity> children) {
        this.children = children;
    }

	public void setId(UUID id) {
		// TODO Auto-generated method stub
		this.id =id;
	}
}