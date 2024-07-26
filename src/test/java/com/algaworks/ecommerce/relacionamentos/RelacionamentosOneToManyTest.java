package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RelacionamentosOneToManyTest extends EntityManagerTest {
    
    @Test
    public void verificarRelacionamentoOneToMany() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(BigDecimal.TEN);
        pedido.setCliente(cliente);
        
        Pedido pedido1 = new Pedido();
        pedido1.setStatus(StatusPedido.PAGO);
        pedido1.setDataPedido(LocalDateTime.now().minusDays(1));
        pedido1.setDataConclusao(LocalDateTime.now());
        pedido1.setTotal(BigDecimal.TEN);
        pedido1.setCliente(cliente);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.persist(pedido1);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteVerificado);
        assertEquals(2, clienteVerificado.getPedidos().size());
        
        System.out.println(clienteVerificado.getPedidos());
    }
    
    @Test
    public void verificarRelacionamentoItemPedidoOneToMany() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setTotal(BigDecimal.TEN);
        pedido.setCliente(cliente);
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPrecoProduto(produto.getPreco());
        itemPedido.setQuantidade(1);
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificado);
        assertEquals(1, pedidoVerificado.getItemPedidos().size());
    }
}