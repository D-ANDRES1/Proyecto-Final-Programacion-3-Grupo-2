package com.grupo2.app.service;

import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.NodeEntity;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.repository.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }
    
    

    
    // 🔹 Crear raíz
    public NodeResponse createRoot(CreateRootRequest request) {

        if (nodeRepository.existsByParentIdIsNull()) {
            throw new RuntimeException("Root ya existe");
        }

        NodeEntity node = new NodeEntity();
        node.setValue(request.getValue());
        node.setParentId(null);

        NodeEntity saved = nodeRepository.save(node);

        return mapToResponse(saved);
    }

    // 🔹 Agregar hijo
    public NodeResponse addChild(String parentId, AddChildRequest request) {

        NodeEntity parent = nodeRepository.findById(parentId)
            .orElseThrow(() -> new IllegalStateException("Parent no existe"));

        NodeEntity child = new NodeEntity();
        child.setValue(request.getValue());
        child.setParentId(parent.getId());

        return mapToResponse(nodeRepository.save(child));
    }

    // 🔹 Listar todo el árbol
    public List<NodeEntity> getAll() {
        return nodeRepository.findAll();
    }
    
    private NodeResponse mapToResponse(NodeEntity entity) {
        NodeResponse dto = new NodeResponse();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setParentId(entity.getParentId());
        return dto;
    }

}