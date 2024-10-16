package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathExpressionTest extends EntityManagerTest {
    
    @Test
    public void usarPathExpressions() {
        String jpql = "select p from Pedido p where p.cliente.nome = 'Guilherme Henrique'";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> pedidos = typedQuery.getResultList();
        assertEquals(2, pedidos.size());
        
        jpql = "select distinct p.cliente.nome from Pedido p where p.cliente.nome = 'Guilherme Henrique'";
        
        TypedQuery<String> typedQuery1 = entityManager.createQuery(jpql, String.class);
        
        String nome = typedQuery1.getSingleResult();
        assertEquals("Guilherme Henrique", nome);
    }
    
    @Test
    public void buscarPedidosComProdutoEspecifico() {
        String jpql = "select p from Pedido p join p.itemPedidos ip where ip.id.produtoId = 1";
//        String jpql = "select p from Pedido p join p.itemPedidos ip where ip.produto.id = 1";
//        String jpql = "select p from Pedido p join p.itemPedidos ip join ip.produto pro where pro.id = 1";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> lista = typedQuery.getResultList();
        assertEquals(2, lista.size());
    }
}