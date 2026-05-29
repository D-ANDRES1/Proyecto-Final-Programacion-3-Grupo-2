package com.grupo2.app.persistence.strategy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.grupo2.treeengine.domain.Node;

public interface PersistenceStrategy {

    Node save(Node node);

    Optional<Node> findById(UUID id);

    List<Node> findChildren(UUID parentId);

    List<Node> findRoots();

    List<Node> findAllByTreeId(UUID treeId);

    void delete(UUID id);

	List<Node> findAll();
	
	 void updateTree(UUID treeId, String computerName, String manufacturer);
	
}

