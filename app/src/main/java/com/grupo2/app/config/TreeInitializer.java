package com.grupo2.app.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.treeengine.domain.Node;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;
import com.grupo2.treeengine.strategy.impl.CustomTreeAlgorithmStrategy;

import jakarta.annotation.PostConstruct;

@Component
@ConditionalOnProperty(name = "app.tree.strategy", havingValue = "custom")
public class TreeInitializer {

    private final ITreeAlgorithmStrategy algorithmStrategy;
    private final PersistenceStrategy persistence;

    public TreeInitializer(ITreeAlgorithmStrategy algorithmStrategy, PersistenceStrategy persistence) {
        this.algorithmStrategy = algorithmStrategy;
        this.persistence = persistence;
    }

    @PostConstruct
    public void hydrateTree() {
        if (algorithmStrategy instanceof CustomTreeAlgorithmStrategy custom) {
            List<Node> nodes = persistence.findAll();
            if (!nodes.isEmpty()) {
                custom.loadFromDatabase(nodes);
                System.out.println("✅ CustomStrategy hidratado: " + nodes.size() + " nodos");
            }
        }
    }
}