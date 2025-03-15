package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RelacionamentosManyToOneTest extends EntityManagerTest {
    
    @Test
    public void verificarRelacionamento() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(BigDecimal.TEN);
        
        pedido.setCliente(cliente);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        
        assertNotNull(pedidoVerificado);
    }
    
    @Test
    public void verificarRelacionamentoItemPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(BigDecimal.TEN);
        pedido.setCliente(cliente);
        
//        entityManager.getTransaction().begin();
//        entityManager.persist(pedido);
//        entityManager.flush();
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPrecoProduto(produto.getPreco());
        itemPedido.setQuantidade(1);
        
//        itemPedido.setId(new ItemPedidoId(pedido.getId(), produto.getId()));
        itemPedido.setId(new ItemPedidoId());
        
//        itemPedido.setPedidoId(pedido.getId());
        itemPedido.setPedido(pedido);
        
//        itemPedido.setProdutoId(produto.getId());
        itemPedido.setProduto(produto);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
//        ItemPedido itemPedidoVerificado = entityManager.find(ItemPedido.class, itemPedido.getId());
        
        ItemPedido itemPedidoVerificado = entityManager.find(ItemPedido.class, new ItemPedidoId(pedido.getId(), produto.getId()));
        assertNotNull(itemPedidoVerificado);
        assertNotNull(itemPedidoVerificado.getPedido());
        assertNotNull(itemPedidoVerificado.getProduto());
    }
}