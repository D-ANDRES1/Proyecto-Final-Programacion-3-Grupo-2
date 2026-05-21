package com.grupo2.app.persistence.strategy;

import com.grupo2.app.mapper.persistence.JpaNodeMapper;
import com.grupo2.app.model.NodeEntity;
import com.grupo2.app.model.TreeEntity;
import com.grupo2.app.repository.NodeRepository;
import com.grupo2.app.repository.TreeRepository;
import com.grupo2.treeengine.domain.Node;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JpaPersistenceStrategy implements PersistenceStrategy {

    private final NodeRepository repository;
    private final TreeRepository treeRepository;
    private final JpaNodeMapper mapper;

    public JpaPersistenceStrategy(
            NodeRepository repository,
            JpaNodeMapper mapper,
            TreeRepository treeRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.treeRepository = treeRepository;
    }

    @Override
    public Node save(Node node) {
        NodeEntity entity = mapper.toEntity(node);

        if (node.getParentId() == null) {
            // Es nodo raíz → crear árbol nuevo
            TreeEntity newTree = new TreeEntity();
            TreeEntity savedTree = treeRepository.save(newTree);
            entity.setTree(savedTree);
        } else {
            // Es hijo → heredar el tree del padre
            NodeEntity parent = repository.findById(node.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found: " + node.getParentId()));
            entity.setTree(parent.getTree());
        }

        NodeEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Node> findById(UUID id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Node> findChildren(UUID parentId) {

        return repository.findByParentId(parentId)
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
                .filter(entity ->
                        entity.getTree() != null
                        && entity.getTree().getId().equals(treeId)
                )
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {

        repository.deleteById(id);
    }
    
    @Override
    public List<Node> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}