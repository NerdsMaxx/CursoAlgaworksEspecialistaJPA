package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPagamento;
import com.algaworks.ecommerce.model.StatusPedido;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassandoParametrosTest extends EntityManagerTest {
    
    @Test
    public void passarParametro() {
        String jpql = "select p from Pedido p join p.pagamento pag where p.id = ?1 and pag.status = ?2";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter(1, 2);
        typedQuery.setParameter(2, StatusPagamento.PROCESSANDO);
        
        List<Pedido> pedidos = typedQuery.getResultList();
        assertEquals(1, pedidos.size());
        
        jpql = "select p from Pedido p join p.pagamento pag where p.id = :pedidoId and pag.status = :pagamentoStatus";
        
        typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("pedidoId", 2);
        typedQuery.setParameter("pagamentoStatus", StatusPagamento.PROCESSANDO);
        
        pedidos = typedQuery.getResultList();
        assertEquals(1, pedidos.size());
    }
    
    @Test
    public void passarParametroDate() {
        String jpql = "select nf from NotaFiscal nf where nf.dataEmissao <= ?1";
        
        TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(jpql, NotaFiscal.class);
        typedQuery.setParameter(1, new Date(), TemporalType.TIMESTAMP);
        
        List<NotaFiscal> notaFiscals = typedQuery.getResultList();
        assertEquals(1, notaFiscals.size());
    }
}