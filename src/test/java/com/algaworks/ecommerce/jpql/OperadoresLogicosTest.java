package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OperadoresLogicosTest extends EntityManagerTest {
    
    @Test
    public void usarOperadores() {
//        String jpql = "select p from Pedido p " +
//                      "where p.total > 100 " +
//                      "and p.status = 'AGUARDANDO' " +
//                      "and p.cliente.id = 1";
        
        String jpql = "select p from Pedido p " +
                      "where (p.status = 'AGUARDANDO' " +
                      "or p.status = 'PAGO') " +
                      "and p.total > 100";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> pedidos = typedQuery.getResultList();
        assertFalse(pedidos.isEmpty());
        
        
        
        jpql = "select p from Pedido p " +
               "where (p.status = 'CANCELADO' " +
               "or p.status = 'PAGO') " +
               "and p.total > 100";
        
        typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        pedidos = typedQuery.getResultList();
        assertTrue(pedidos.isEmpty());
    }
}