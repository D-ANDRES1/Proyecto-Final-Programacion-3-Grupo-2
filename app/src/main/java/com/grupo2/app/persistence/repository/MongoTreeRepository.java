package com.grupo2.app.persistence.repository;

import com.grupo2.app.model.MongoTree;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoTreeRepository
        extends MongoRepository<MongoTree, String> {
}