package com.grupo2.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Documento MongoDB para representar un nodo del árbol.
 * NO es una entidad JPA, es un documento NoSQL.
 */
@Document(collection = "nodes")
public class MongoNode {

    @Id
    private String id;

    @Field("value")
    private String value;

    @Field("parent_id")
    private String parentId;
    
    @Field("tree_id")
    private String treeId;

    // Constructor vacío (requerido por Spring Data Mongo)
    public MongoNode() {
    }

    // Constructor con parámetros
    public MongoNode(String value, String parentId) {
        this.value = value;
        this.parentId = parentId;
    }
    
    public MongoNode(String value, String parentId, String treeId) {
        this.value = value;
        this.parentId = parentId;
        this.treeId = treeId;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    
    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }
}