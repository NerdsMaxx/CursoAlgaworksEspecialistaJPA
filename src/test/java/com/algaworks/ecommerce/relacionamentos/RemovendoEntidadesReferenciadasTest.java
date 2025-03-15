package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RemovendoEntidadesReferenciadasTest extends EntityManagerTest {

    @Test
    public void removerEntidadeRelacionada() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        assertFalse(pedido.getItemPedidos().isEmpty());
        
        entityManager.getTransaction().begin();
        pedido.getItemPedidos().forEach(entityManager::remove);
        entityManager.remove(pedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, 1);
        assertNull(pedidoVerificado);
    }
}