package com.grupo2.app.persistence.repository;

import com.grupo2.app.model.MongoNode;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoNodeRepository
        extends MongoRepository<MongoNode, String> {

    List<MongoNode> findByParentId(
            String parentId
    );

    List<MongoNode> findByParentIdIsNull();

    List<MongoNode> findByTreeId(
            String treeId
    );
}