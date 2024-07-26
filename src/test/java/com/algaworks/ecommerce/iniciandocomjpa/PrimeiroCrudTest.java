package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimeiroCrudTest extends EntityManagerTest {

    @Test
    public void inserirRegistro() {
        Cliente cliente = new Cliente();
        
//        cliente.setId(3);
//        Se usar @GeneratedValue(strategy = GenerationType.IDENTITY),
//        não precisa setar o Id.
        cliente.setNome("Márcio Antusa");
        
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, 3);
        assertNotNull(clienteVerificado);
    }
    
    @Test
    public void buscarPorIdentificador() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        
        assertNotNull(cliente);
        assertEquals(1, cliente.getId());
        assertEquals("Guilherme Henrique", cliente.getNome());
    }
    
    @Test
    public void atualizarRegistro() {
        Cliente cliente = entityManager.find(Cliente.class, 2);
        
//        cliente.setId(2);
        cliente.setNome("Watyson Guimarães");
        
        entityManager.getTransaction().begin();
        entityManager.merge(cliente);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, 2);
        assertNotNull(clienteVerificado);
        assertEquals("Watyson Guimarães", cliente.getNome());
    }
    
    @Test
    public void removerRegistro() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        
        entityManager.getTransaction().begin();
        entityManager.remove(cliente);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, 1);
        assertNull(clienteVerificado);
    }
}