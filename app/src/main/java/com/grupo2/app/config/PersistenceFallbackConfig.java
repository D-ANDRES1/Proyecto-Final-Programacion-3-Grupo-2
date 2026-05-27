package com.grupo2.app.config;

import com.grupo2.app.persistence.strategy.PersistenceStrategy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceFallbackConfig {

    @Bean
    @ConditionalOnMissingBean(PersistenceStrategy.class)
    public PersistenceStrategy defaultPersistenceStrategy() {
        throw new IllegalStateException(
            "No persistence strategy configured. Use app.persistence.type=h2 or mongo"
        );
    }
}