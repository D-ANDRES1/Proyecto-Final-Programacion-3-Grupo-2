package com.grupo2.app.config;


import com.grupo2.app.mapper.persistence.JpaNodeMapper;
import com.grupo2.app.persistence.strategy.JpaPersistenceStrategy;
import com.grupo2.app.persistence.strategy.PersistenceStrategy;
import com.grupo2.app.repository.NodeRepository;
import com.grupo2.app.repository.TreeRepository;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(name = "app.persistence.type", havingValue = "jpa", matchIfMissing = true)
@EnableJpaRepositories(basePackages = "com.grupo2.app.repository")
public class JpaPersistenceConfig {

    @Bean
    public PersistenceStrategy jpaPersistenceStrategy(
            NodeRepository repository,
            TreeRepository treeRepository,
            JpaNodeMapper mapper
    ) {
        return new JpaPersistenceStrategy(repository, mapper, treeRepository);
    }
}