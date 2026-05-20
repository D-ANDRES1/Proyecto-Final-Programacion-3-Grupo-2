package com.grupo2.app.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.grupo2.app.mapper.persistence.JpaNodeMapper;
import com.grupo2.app.persistence.strategy.JpaPersistenceStrategy;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.app.repository.NodeRepository;
import com.grupo2.app.repository.TreeRepository;

@Configuration
public class PersistenceConfig {
	
	
	
	@Bean
    @ConditionalOnProperty(
        name = "app.persistence.type",
        havingValue = "h2"
    )
    public PersistenceStrategy jpaPersistenceStrategy(
            NodeRepository repository,
            TreeRepository treeRepository,
            JpaNodeMapper mapper
    ) {
        return new JpaPersistenceStrategy(repository, mapper, treeRepository);
    }

	@Bean
    @Primary
    @ConditionalOnMissingBean(PersistenceStrategy.class)
    public PersistenceStrategy defaultPersistence() {
        throw new IllegalStateException("No persistence.type configured");
    }
	// FUTURO MONGO
    /*
    @Bean
    @ConditionalOnProperty(
        name = "persistence.type",
        havingValue = "mongo"
    )
    public PersistenceStrategy mongoPersistenceStrategy(
            MongoNodeRepository repo,
            MongoMapper mapper
    ) {
        return new MongoPersistenceStrategy(repo, mapper);
    }
    */
	
	
	
    // Bean para ESTRATEGIA MONGO (Parte C)
//    @Bean
//    @ConditionalOnProperty(name = "persistence.type", havingValue = "mongo")
//    public PersistenceStrategy mongoPersistenceStrategy(
//            MongoNodeRepository mongoRepository,
//            MongoMapper mongoMapper) {
//        return new MongoPersistenceStrategy(mongoRepository, mongoMapper);
//    }
}
