package com.algaworks.ecommerce;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.Duration;

public abstract class EntityManagerFactoryTest {

    protected static EntityManagerFactory entityManagerFactory;
    
    @BeforeAll
    public static void beforeAll() {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU");
    }
    
    @AfterAll
    public static void afterAll() {
        entityManagerFactory.close();
    }
    
    public void esperar(int segundos) {
        try {
            Thread.sleep(Duration.ofSeconds(segundos).toMillis());
        } catch (InterruptedException e) {}
    }
    
}