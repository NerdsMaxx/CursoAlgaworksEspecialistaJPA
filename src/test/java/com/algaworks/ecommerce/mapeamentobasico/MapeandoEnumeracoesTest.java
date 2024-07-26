package com.algaworks.ecommerce.mapeamentobasico;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.SexoCliente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapeandoEnumeracoesTest extends EntityManagerTest {

    @Test
    public void testarEnum() {
        Cliente cliente = new Cliente();
        
//        cliente.setId(4);
        cliente.setNome("José Maria");
        cliente.setSexo(SexoCliente.MASCULINO);
        
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificacao = entityManager.find(Cliente.class, 4);
        assertNotNull(clienteVerificacao);
    }
}