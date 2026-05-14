package com.grupo2.app.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nodes")
public class NodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "node_value")
    private String value;

    private String parentId;

    // opcional (solo para navegación en memoria, NO obligatorio en DB)
    @Transient
    private List<NodeEntity> children = new ArrayList<>();
    
    public String getId() {
        return id;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<NodeEntity> getChildren() {
		return children;
	}

	public void setChildren(List<NodeEntity> children) {
		this.children = children;
	}

    // getters y setters
    
}