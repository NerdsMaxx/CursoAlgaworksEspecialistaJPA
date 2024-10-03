package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HerancaTest extends EntityManagerTest {
    
    @Test
    public void salvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Fernanda Morais");
        
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, cliente.getId());
        assertNotNull(clienteVerificado.getId());
    }
    
    @Test
    public void buscarPagamentos() {
        List<Pagamento> pagamentos = entityManager
                .createQuery("select p from Pagamento p")
                .getResultList();
        
        assertFalse(pagamentos.isEmpty());
    }
    
    @Test
    public void incluirPagamentoPedido() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        PagamentoCartao pagamentoCartao = new PagamentoCartao();
        pagamentoCartao.setPedido(pedido);
        pagamentoCartao.setStatus(StatusPagamento.PROCESSANDO);
        pagamentoCartao.setNumeroCartao("123456789");
        
        entityManager.getTransaction().begin();
        entityManager.persist(pagamentoCartao);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificado.getPagamento());
    }
}