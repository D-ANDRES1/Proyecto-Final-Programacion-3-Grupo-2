package com.grupo2.app.persistence.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.grupo2.app.mapper.RequestMapper;
import com.grupo2.app.mapper.ResponseMapper;
import com.grupo2.app.model.NodeEntity;
import com.grupo2.app.repository.NodeRepository;
import com.grupo2.treeengine.core.TreeNode;

@Component
@Profile({"postgres", "h2"})
public class JpaPersistenceStrategy implements PersistenceStrategy {

    private final NodeRepository nodeRepository;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    public JpaPersistenceStrategy(NodeRepository nodeRepository,
                                  RequestMapper requestMapper,
                                  ResponseMapper responseMapper) {
        this.nodeRepository = nodeRepository;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public TreeNode createRoot(TreeNode node) {
        // 1. Convertir TreeNode → NodeEntity
        NodeEntity entity = requestMapper.toEntity(node);
        // 2. Setear parentId y depth en NodeEntity (NO en TreeNode)
        entity.setParentId(null); // raíz no tiene padre
        // entity.setDepth(0); // 👈 TU NodeEntity NO tiene depth, lo quitamos
        
        // 3. Guardar en BD
        NodeEntity saved = nodeRepository.save(entity);
        
        // 4. Convertir de vuelta a TreeNode
        return responseMapper.toDomain(saved);
    }

    @Override
    public TreeNode addChild(String parentId, TreeNode childNode) {
        // 1. Convertir TreeNode → NodeEntity
        NodeEntity entity = requestMapper.toEntity(childNode);
        // 2. Setear parentId en NodeEntity
        entity.setParentId(parentId);
        // entity.setDepth(1); // 👈 TU NodeEntity NO tiene depth, lo quitamos
        
        // 3. Guardar
        NodeEntity saved = nodeRepository.save(entity);
        
        // 4. Devolver como TreeNode
        return responseMapper.toDomain(saved);
    }

    @Override
    public Optional<TreeNode> findById(String id) {
        return nodeRepository.findById(id)
                .map(entity -> responseMapper.toDomain(entity));
    }

    @Override
    public List<TreeNode> findAll() {
        List<NodeEntity> entities = nodeRepository.findAll();
        List<TreeNode> result = new ArrayList<>();
        for (NodeEntity entity : entities) {
            result.add(responseMapper.toDomain(entity));
        }
        return result;
    }

    @Override
    public List<TreeNode> findChildren(String parentId) {
        List<NodeEntity> entities = nodeRepository.findByParentId(parentId);
        List<TreeNode> result = new ArrayList<>();
        for (NodeEntity entity : entities) {
            result.add(responseMapper.toDomain(entity));
        }
        return result;
    }

    @Override
    public void delete(String id) {
        nodeRepository.deleteById(id);
    }
}