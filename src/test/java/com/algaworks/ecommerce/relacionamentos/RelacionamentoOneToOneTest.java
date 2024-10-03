package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RelacionamentoOneToOneTest extends EntityManagerTest {
    
    @Test
    public void verificarPagamentoCartaoComPedido() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        PagamentoCartao pagamentoCartao = new PagamentoCartao();
        pagamentoCartao.setNumeroCartao("1234");
        pagamentoCartao.setStatus(StatusPagamento.PROCESSANDO);
        pagamentoCartao.setPedido(pedido);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pagamentoCartao);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificado.getPagamento());
    }
    
    @Test
    public void verificarNotaFiscalComPedido() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        NotaFiscal notaFiscal = new NotaFiscal();
        notaFiscal.setPedido(pedido);
        notaFiscal.setDataEmissao(new Date());
        
        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificado.getNotaFiscal());
    }
}