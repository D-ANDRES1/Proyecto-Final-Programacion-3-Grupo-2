package com.grupo2.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.grupo2.app.persistence.strategy.PersistenceStrategy;

@SpringBootTest
@ActiveProfiles("h2")
class ProyectoFinalApplicationTests {

    // 👇 ESTO ES LO IMPORTANTE: Mockeamos la estrategia para que el test no falle
    @MockBean
    private PersistenceStrategy persistenceStrategy;

    @Test
    void contextLoads() {
        // Este test solo verifica que la aplicación arranque
        // No prueba la lógica real, solo que Spring pueda cargar el contexto
    }
}