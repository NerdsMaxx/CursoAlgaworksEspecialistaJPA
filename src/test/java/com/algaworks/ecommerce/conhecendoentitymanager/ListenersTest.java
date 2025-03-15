package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ListenersTest extends EntityManagerTest {
    
    @Test
    public void acionarCallbacks() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Pedido pedido = new Pedido();
        
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setTotal(BigDecimal.TEN);
        
        entityManager.getTransaction().begin();
        
        entityManager.persist(pedido);
        entityManager.flush();
        
        pedido.setStatus(StatusPedido.PAGO);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        
        assertNotNull(pedidoVerificacao.getDataCriacao());
        assertEquals(pedidoVerificacao.getDataCriacao().toLocalDate(), LocalDate.now());
        
        assertNotNull(pedidoVerificacao.getDataUltimaAtualizacao());
        assertEquals(pedidoVerificacao.getDataUltimaAtualizacao().toLocalDate(), LocalDate.now());
    }
}