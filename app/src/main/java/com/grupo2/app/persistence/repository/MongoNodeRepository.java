package com.grupo2.app.persistence.repository;

import com.grupo2.app.model.MongoNode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoNodeRepository extends MongoRepository<MongoNode, String> {
    
    List<MongoNode> findByParentId(String parentId);
    
    boolean existsByParentIdIsNull();
    
    MongoNode findByParentIdIsNull();
}