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

        TreeEntity tree = treeRepository
                .findById(node.getTreeId())
                .orElseGet(() -> {
                    TreeEntity newTree = new TreeEntity();
                    newTree.setId(node.getTreeId());
                    return treeRepository.save(newTree);
                });

        entity.setTree(tree);

        NodeEntity saved = repository.save(entity);

        // ✅ recargar desde BD para que tree esté disponible
        NodeEntity reloaded = repository.findById(saved.getId())
                .orElse(saved);

        return mapper.toDomain(reloaded);
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
        return repository.findByParentIdIsNull()  // ✅ Devuelve List<NodeEntity>
                .stream()
                .map(mapper::toDomain)            // ✅ mapper.toDomain(NodeEntity) funciona
                .collect(Collectors.toList());
    }

    @Override
    public List<Node> findAllByTreeId(UUID treeId) {

        return repository.findByTree_Id(treeId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void delete(UUID id) {

        repository.deleteById(id);
    }
    
    @Override
    public List<Node> findAll() {
        return repository.findAll()  // ✅ Heredado de JpaRepository<NodeEntity, UUID>
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
    
 // =====================================================
 // UPDATE TREE
 // =====================================================

 @Override
 public void updateTree(UUID treeId, String computerName, String manufacturer) {

     TreeEntity tree = treeRepository.findById(treeId)
             .orElseThrow(() ->
                     new RuntimeException("Tree not found: " + treeId));

     tree.setComputerName(computerName);
     tree.setManufacturer(manufacturer);

     treeRepository.save(tree);
 }
}