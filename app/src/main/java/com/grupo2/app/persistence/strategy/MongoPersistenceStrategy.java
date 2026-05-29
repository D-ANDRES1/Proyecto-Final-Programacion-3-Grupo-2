package com.grupo2.app.persistence.strategy;

import com.grupo2.app.mapper.persistence.MongoNodeMapper;
import com.grupo2.app.model.MongoNode;
import com.grupo2.app.model.MongoTree;
import com.grupo2.app.persistence.repository.MongoNodeRepository;
import com.grupo2.app.persistence.repository.MongoTreeRepository;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.app.model.MongoTree;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


public class MongoPersistenceStrategy
        implements PersistenceStrategy {

    private final MongoNodeRepository repository;

    private final MongoNodeMapper mapper;
    
    private final MongoTreeRepository treeRepository;

    public MongoPersistenceStrategy(
            MongoNodeRepository repository,
            MongoNodeMapper mapper,
            MongoTreeRepository treeRepository
    ) {

        this.repository = repository;
        this.mapper = mapper;
        this.treeRepository = treeRepository; 
    }

    @Override
    public Node save(Node node) {

        MongoNode entity = mapper.toEntity(node);

        MongoNode saved = repository.save(entity);

        // Si es raíz, crear documento en trees
        if (node.getParentId() == null) {
            MongoTree tree = new MongoTree();
            tree.setId(node.getTreeId().toString());
            treeRepository.save(tree);
        }

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Node> findById(UUID id) {

        return repository.findById(id.toString())
                .map(mapper::toDomain);
    }

    @Override
    public List<Node> findChildren(
            UUID parentId
    ) {

        return repository.findByParentId(
                        parentId.toString()
                )
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Node> findRoots() {

        return repository.findByParentIdIsNull()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Node> findAllByTreeId(
            UUID treeId
    ) {

        return repository.findByTreeId(
                        treeId.toString()
                )
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Node> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {

        repository.deleteById(id.toString());
    }
 // =====================================================
 // UPDATE TREE
 // =====================================================

    @Override
    public void updateTree(UUID treeId, String computerName, String manufacturer) {

        MongoTree tree = treeRepository.findById(treeId.toString())
                .orElseThrow(() ->
                        new RuntimeException("Tree not found: " + treeId));

        tree.setComputerName(computerName);
        tree.setManufacturer(manufacturer);

        treeRepository.save(tree);
    }
}