package com.grupo2.app.persistence.strategy;

import com.grupo2.app.mapper.persistence.MongoNodeMapper;
import com.grupo2.app.model.MongoNode;
import com.grupo2.app.persistence.repository.MongoNodeRepository;
import com.grupo2.treeengine.domain.Node;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MongoPersistenceStrategy implements PersistenceStrategy {

    private final MongoNodeRepository repository;
    private final MongoNodeMapper mapper;

    public MongoPersistenceStrategy(
            MongoNodeRepository repository,
            MongoNodeMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Node save(Node node) {

        MongoNode mongoNode = mapper.toEntity(node);

        MongoNode savedNode = repository.save(mongoNode);

        return mapper.toDomain(savedNode);
    }

    @Override
    public Optional<Node> findById(UUID id) {

        return repository.findById(id.toString())
                .map(mapper::toDomain);
    }

    @Override
    public List<Node> findChildren(UUID parentId) {

        return repository.findByParentId(parentId.toString())
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
    public List<Node> findAllByTreeId(UUID treeId) {

        return repository.findAll()
                .stream()
                .filter(node ->
                        node.getTreeId() != null
                        && node.getTreeId().equals(treeId.toString())
                )
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {

        repository.deleteById(id.toString());
    }
    
    @Override
    public List<Node> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}