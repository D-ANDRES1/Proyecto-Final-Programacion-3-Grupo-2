package com.grupo2.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "nodes")
public class NodeEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "node_value", length = 255, nullable = false)
    @NotBlank(message = "El valor del nodo no puede estar vacío")
    @Size(min = 1, max = 64, message = "El valor debe tener entre 1 y 64 caracteres")
    private String value;

    @Column(name = "parent_id", length = 50)
    private String parentId;

    @Transient
    private java.util.List<NodeEntity> children = new java.util.ArrayList<>();

    // Getters y Setters
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

    public java.util.List<NodeEntity> getChildren() {
        return children;
    }

    public void setChildren(java.util.List<NodeEntity> children) {
        this.children = children;
    }
}