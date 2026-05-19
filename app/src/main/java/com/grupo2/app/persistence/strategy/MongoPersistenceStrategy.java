package com.grupo2.app.persistence.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.grupo2.app.mapper.MongoMapper;
import com.grupo2.app.model.MongoNode;
import com.grupo2.app.persistence.repository.MongoNodeRepository;
import com.grupo2.treeengine.core.TreeNode;
import com.grupo2.treeengine.exception.InvalidNodeValueException;
import com.grupo2.treeengine.exception.NodeNotFoundException;
import com.grupo2.treeengine.exception.RootAlreadyExistsException;

@Component
@Profile({"mongo", "mongodb"})
public class MongoPersistenceStrategy implements PersistenceStrategy {

    private final MongoNodeRepository mongoRepository;
    private final MongoMapper mongoMapper;

    public MongoPersistenceStrategy(MongoNodeRepository mongoRepository,
                                    MongoMapper mongoMapper) {
        this.mongoRepository = mongoRepository;
        this.mongoMapper = mongoMapper;
    }

    @Override
    public TreeNode createRoot(TreeNode node) {
        // Validar valor del nodo
        if (node.getValue() == null || node.getValue().isBlank()) {
            throw new InvalidNodeValueException(node.getValue());
        }

        // Validar que no exista ya una raíz
        if (mongoRepository.existsByParentIdIsNull()) {
            throw new RootAlreadyExistsException();
        }

        String id = node.getId() != null ? node.getId() : UUID.randomUUID().toString();
        TreeNode newNode = new TreeNode(id, node.getValue());
        return saveTreeNode(newNode, null);
    }

    @Override
    public TreeNode addChild(String parentId, TreeNode childNode) {
        // Validar valor del nodo
        if (childNode.getValue() == null || childNode.getValue().isBlank()) {
            throw new InvalidNodeValueException(childNode.getValue());
        }

        // Validar que el padre exista
        if (!mongoRepository.existsById(parentId)) {
            throw new NodeNotFoundException(parentId);
        }

        String id = childNode.getId() != null ? childNode.getId() : UUID.randomUUID().toString();
        TreeNode newNode = new TreeNode(id, childNode.getValue());
        return saveTreeNode(newNode, parentId);
    }

    @Override
    public Optional<TreeNode> findById(String id) {
        Optional<MongoNode> optionalNode = mongoRepository.findById(id);
        if (optionalNode.isPresent()) {
            return Optional.of(mongoMapper.toDomain(optionalNode.get()));
        }
        // Opcional: lanzar excepción si no se encuentra
        // throw new NodeNotFoundException(id);
        return Optional.empty();
    }

    @Override
    public List<TreeNode> findAll() {
        List<MongoNode> nodes = mongoRepository.findAll();
        List<TreeNode> result = new ArrayList<>();
        for (MongoNode node : nodes) {
            result.add(mongoMapper.toDomain(node));
        }
        return result;
    }

    @Override
    public List<TreeNode> findChildren(String parentId) {
        // Validar que el padre exista
        if (!mongoRepository.existsById(parentId)) {
            throw new NodeNotFoundException(parentId);
        }

        List<MongoNode> children = mongoRepository.findByParentId(parentId);
        List<TreeNode> result = new ArrayList<>();
        for (MongoNode node : children) {
            result.add(mongoMapper.toDomain(node));
        }
        return result;
    }

    @Override
    public void delete(String id) {
        mongoRepository.deleteById(id);
        List<MongoNode> children = mongoRepository.findByParentId(id);
        for (MongoNode child : children) {
            delete(child.getId());
        }
    }

    private TreeNode saveTreeNode(TreeNode node, String parentId) {
        MongoNode mongoNode = mongoMapper.toMongoNode(node);
        mongoNode.setParentId(parentId);
        MongoNode saved = mongoRepository.save(mongoNode);
        return mongoMapper.toDomain(saved);
    }
}