package com.algaworks.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.TimeZone;

public abstract class EntityManagerTest {
    
    protected static EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
    
    @BeforeAll
    public static void beforeAll() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU");
        System.out.println("olá");
    }
    
    @AfterAll
    public static void afterAll() {
        entityManagerFactory.close();
        System.out.println("olá");
    }
    
    @BeforeEach
    public void beforeEach() {
        entityManager = entityManagerFactory.createEntityManager();
    }
    
    @AfterEach
    public void afterEach() {
        entityManager.close();
    }
  
}