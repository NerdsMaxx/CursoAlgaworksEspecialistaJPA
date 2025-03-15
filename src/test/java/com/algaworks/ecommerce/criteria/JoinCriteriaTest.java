package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JoinCriteriaTest extends EntityManagerTest {
    
    @Test
    public void fazerJoin() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido,Pagamento> join = root.join("pagamento");
        
        criteriaQuery.select(root);
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(4, resultado.size());
        
        
        
        CriteriaBuilder criteriaBuilder1 = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pagamento> criteriaQuery1 = criteriaBuilder1.createQuery(Pagamento.class);
        Root<Pedido> root1 = criteriaQuery1.from(Pedido.class);
        Join<Pedido,Pagamento> join1 = root1.join("pagamento");
        
        criteriaQuery1.select(join1);
        criteriaQuery1.where(criteriaBuilder1.equal(
                join1.get("status"), StatusPagamento.PROCESSANDO));
        
        TypedQuery<Pagamento> typedQuery1 = entityManager.createQuery(criteriaQuery1);
        
        List<Pagamento> resultado1 = typedQuery1.getResultList();
        
        assertFalse(resultado1.isEmpty());
        assertEquals(2, resultado1.size());
        
        
        
        CriteriaBuilder criteriaBuilder2 = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery2 = criteriaBuilder2.createQuery(Produto.class);
        Root<Pedido> root2 = criteriaQuery2.from(Pedido.class);
        
        //Pode encadear joins
        Join<Pedido,ItemPedido> join2 = root2.join("itemPedidos");
        Join<ItemPedido,Produto> joinProduto = join2.join("produto");
        
        criteriaQuery2.select(joinProduto);
        
        TypedQuery<Produto> typedQuery2 = entityManager.createQuery(criteriaQuery2);
        
        List<Produto> resultado2 = typedQuery2.getResultList();
        
        assertFalse(resultado2.isEmpty());
        
        System.out.println("Mostrando produtos");
        resultado2.forEach(p -> System.out.println("id = " + p.getId() +
                                                   ", nome = " + p.getNome()));
    }
    
    
    @Test
    public void fazerJoinOn() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido,Pagamento> joinPagamento = root.join("pagamento");
        joinPagamento.on(criteriaBuilder.equal(
                joinPagamento.get("status"), StatusPagamento.PROCESSANDO));
        
        criteriaQuery.select(root);
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
    }
    
    
    @Test
    public void fazerLeftOuterJoin() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido,Pagamento> joinPagamento = root.join("pagamento", JoinType.LEFT);
        
        criteriaQuery.select(root);
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(8, resultado.size());
    }
    
    @Test
    public void fazerJoinFetch() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
//        root.fetch("itemPedidos");
        
        //usando fetch pode reduzir select em uma única select, ao invés de vários
        // select, mesmo que esteja marcado como EAGER.
        root.fetch("notaFiscal", JoinType.LEFT);
        root.fetch("pagamento", JoinType.LEFT);
        root.fetch("cliente");
        
        //Join<Pedido, Cliente> joinCliente = (Join<Pedido, Cliente>) root.<Pedido, Cliente>fetch("cliente");
        
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        Pedido resultado = typedQuery.getSingleResult();
        
        
        assertNotNull(resultado);
//        assertFalse(resultado.getItemPedidos().isEmpty());
    }
    
    @Test
    public void buscarPedidosComProdutoEspecifico() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<ItemPedido, Produto> joinItemPedidoProduto = root
                .join("itemPedidos")
                .join("produto");
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.equal(
                joinItemPedidoProduto.get("id"), 1));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        
        resultado.forEach(p -> System.out.println("id = " + p.getId() + ", total = " + p.getTotal()));
    }
}