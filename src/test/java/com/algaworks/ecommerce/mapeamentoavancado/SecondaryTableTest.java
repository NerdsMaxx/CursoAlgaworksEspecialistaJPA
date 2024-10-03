package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.SexoCliente;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecondaryTableTest extends EntityManagerTest {
    
    @Test
    public void salvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Finotti");
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteVerificado.getSexo());
        assertNotNull(clienteVerificado.getDataNascimento());
    }
}