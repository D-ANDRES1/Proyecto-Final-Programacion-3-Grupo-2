package com.grupo2.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.grupo2.app.mapper.MongoMapper;
import com.grupo2.app.persistence.repository.MongoNodeRepository;
import com.grupo2.app.persistence.strategy.MongoPersistenceStrategy;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
public class TreeStrategyConfig {

    // Bean para ESTRATEGIA DE MEMORIA (Parte A)
    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "memory")
    public String estrategiaMemoria() {
        System.out.println(">>> Activando estrategia: MEMORIA");
        return "MEMORIA_ACTIVA";
    }

    // Bean para ESTRATEGIA POSTGRESQL (Parte B)
    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "postgres")
    public String estrategiaPostgres() {
        System.out.println(">>> Activando estrategia: POSTGRES");
        return "POSTGRES_ACTIVA";
    }

    // Bean para ESTRATEGIA MONGO (Parte C)
    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "mongo")
    public PersistenceStrategy mongoPersistenceStrategy(
            MongoNodeRepository mongoRepository,
            MongoMapper mongoMapper) {
        return new MongoPersistenceStrategy(mongoRepository, mongoMapper);
    }
}