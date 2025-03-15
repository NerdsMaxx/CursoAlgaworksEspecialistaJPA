package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GerenciamentoTransacoesTest extends EntityManagerTest {
    
    @Test
    public void sobreRollback() {
        Pedido pedido = entityManager.find(Pedido.class, 1);

        entityManager.getTransaction().begin();
        pedido.setStatus(StatusPedido.PAGO);

        if(pedido.getPagamento() != null) {
            entityManager.getTransaction().commit();
        }
        else {
            entityManager.getTransaction().rollback();
        }
    }
    
    @Test
    public void melhorDoQueRollback() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        entityManager.getTransaction().begin();
        
        if(pedido.getPagamento() != null) {
            pedido.setStatus(StatusPedido.PAGO);
        }
        
        entityManager.getTransaction().commit();
    }
    
    @Test
    public void testarLancamentoDeExcecao() {
        Assertions.assertThrows(Exception.class, this::lancarExcecao);
    }
    
    private void lancarExcecao() {
        try {
            entityManager.getTransaction().begin();
            metodoDeNegocio();
            entityManager.getTransaction().commit();
        }
        catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
    
    private void metodoDeNegocio() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        pedido.setStatus(StatusPedido.PAGO);
        
        if(pedido.getPagamento() == null) {
            throw new RuntimeException("Pedido ainda não foi pago.");
        }
    }
}