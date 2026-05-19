package com.grupo2.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.grupo2.app.model.AddChildRequest;
import com.grupo2.app.model.CreateRootRequest;
import com.grupo2.app.model.NodeEntity;
import com.grupo2.app.model.NodeResponse;
import com.grupo2.app.repository.NodeRepository;
import com.grupo2.treeengine.exception.InvalidNodeValueException;
import com.grupo2.treeengine.exception.NodeNotFoundException;
import com.grupo2.treeengine.exception.RootAlreadyExistsException;

@Service
public class NodeService {

    private final NodeRepository nodeRepository;

    public NodeService(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }
    
    // 🔹 Crear raíz
    public NodeResponse createRoot(CreateRootRequest request) {
        
        // Validar valor del nodo
        if (request.getValue() == null || request.getValue().isBlank()) {
            throw new InvalidNodeValueException(request.getValue());
        }

        // Validar que no exista ya una raíz
        if (nodeRepository.existsByParentIdIsNull()) {
            throw new RootAlreadyExistsException();
        }

        NodeEntity node = new NodeEntity();
        node.setValue(request.getValue());
        node.setParentId(null);

        NodeEntity saved = nodeRepository.save(node);

        return mapToResponse(saved);
    }

    // 🔹 Agregar hijo
    public NodeResponse addChild(String parentId, AddChildRequest request) {
        
        // Validar valor del nodo
        if (request.getValue() == null || request.getValue().isBlank()) {
            throw new InvalidNodeValueException(request.getValue());
        }

        // Validar que el padre exista
        NodeEntity parent = nodeRepository.findById(parentId)
            .orElseThrow(() -> new NodeNotFoundException(parentId));

        NodeEntity child = new NodeEntity();
        child.setValue(request.getValue());
        child.setParentId(parent.getId());

        return mapToResponse(nodeRepository.save(child));
    }

    // 🔹 Listar todo el árbol
    public List<NodeEntity> getAll() {
        return nodeRepository.findAll();
    }
    
    // 🔹 Método auxiliar para mapear entidad → DTO
    private NodeResponse mapToResponse(NodeEntity entity) {
        NodeResponse dto = new NodeResponse();
        dto.setId(entity.getId());
        dto.setValue(entity.getValue());
        dto.setParentId(entity.getParentId());
        return dto;
    }
}