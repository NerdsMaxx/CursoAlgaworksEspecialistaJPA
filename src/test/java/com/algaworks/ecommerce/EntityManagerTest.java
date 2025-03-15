package com.algaworks.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.time.Duration;
import java.util.TimeZone;

public abstract class EntityManagerTest extends  EntityManagerFactoryTest {
    
    protected EntityManager entityManager;
    
    @BeforeEach
    public void beforeEach() {
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    @AfterEach
    public void afterEach() {
        entityManager.close();
    }
}