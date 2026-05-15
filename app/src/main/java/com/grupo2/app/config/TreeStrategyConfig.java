package com.grupo2.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    // Bean para ESTRATEGIA MONGO (Parte C - STUB por ahora)
    @Bean
    @ConditionalOnProperty(name = "app.tree-strategy", havingValue = "mongo")
    public String estrategiaMongo() {
        System.out.println(">>> Activando estrategia: MONGODB (stub)");
        return "MONGO_ACTIVA";
    }
}