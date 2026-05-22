package com.grupo2.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.grupo2.app.persistence.repository.MongoNodeRepository;
import com.grupo2.app.persistence.strategy.MongoPersistenceStrategy;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.treeengine.service.TreeService;
import com.grupo2.treeengine.strategy.ITreeAlgorithmStrategy;
import com.grupo2.treeengine.strategy.impl.CollectionsTreeAlgorithmStrategy;
import com.grupo2.treeengine.strategy.impl.CustomTreeAlgorithmStrategy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
public class TreeStrategyConfig {

	
	@Bean
	@ConditionalOnProperty(
			name = "app.tree.strategy",
			havingValue = "custom")
	public ITreeAlgorithmStrategy customStrategy() {
		return new CustomTreeAlgorithmStrategy();
	}
	
	@Bean
	@ConditionalOnProperty(
	        name = "app.tree.strategy",
	        havingValue = "collections")
	public ITreeAlgorithmStrategy collectionsStrategy() {
	    return new CollectionsTreeAlgorithmStrategy();
	}
	
    @Bean
    public TreeService treeService(ITreeAlgorithmStrategy strategy) {
    	return new TreeService(strategy);
    }
}
