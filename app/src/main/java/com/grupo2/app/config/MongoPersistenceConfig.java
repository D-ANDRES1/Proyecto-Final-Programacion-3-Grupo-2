package com.grupo2.app.config;

import com.grupo2.app.mapper.persistence.MongoNodeMapper;
import com.grupo2.app.persistence.repository.MongoNodeRepository;
import com.grupo2.app.persistence.strategy.MongoPersistenceStrategy;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ConditionalOnProperty(name = "app.persistence.type", havingValue = "mongo")
@EnableMongoRepositories(basePackages = "com.grupo2.app.persistence.repository")
public class MongoPersistenceConfig {

    @Bean
    public PersistenceStrategy mongoPersistenceStrategy(
            MongoNodeRepository repository,
            MongoNodeMapper mapper
    ) {
        return new MongoPersistenceStrategy(repository, mapper);
    }
}