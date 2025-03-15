package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FuncoesCriteriaTest extends EntityManagerTest {
    
    @Test
    public void aplicarFuncaoString() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);
        
        criteriaQuery.multiselect(
                root.get(Cliente_.nome),
                criteriaBuilder.concat("Nome do cliente: ", root.get(Cliente_.nome)),
                criteriaBuilder.length(root.get(Cliente_.nome)),
                criteriaBuilder.locate(root.get(Cliente_.nome), "a"),
                criteriaBuilder.substring(root.get(Cliente_.nome), 1, 2),
                criteriaBuilder.lower(root.get(Cliente_.nome)),
                criteriaBuilder.upper(root.get(Cliente_.nome)),
                criteriaBuilder.trim(root.get(Cliente_.nome)));
        
        //tem como usar dentro do where
        criteriaQuery.where(criteriaBuilder.equal(
                criteriaBuilder.substring(root.get(Cliente_.nome), 1, 1), "M"));
        
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                r[0]
                + "\nconcat: " + r[1]
                + "\nlength: " + r[2]
                + "\nlocate: " + r[3]
                + "\nsubstring: " + r[4]
                + "\nlower: " + r[5]
                + "\nupper: " + r[6]
                + "\ntrim: |" + r[7] + "|\n\n\n"));
    }
    
    @Test
    public void aplicarFuncaoData() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        Join<Pedido,Pagamento> joinPagamento = root.join(Pedido_.pagamento);
        Join<Pedido,PagamentoBoleto> joinPagamentoBoleto = criteriaBuilder
                .treat(joinPagamento, PagamentoBoleto.class);
        
        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.currentDate(),
                criteriaBuilder.currentTime(),
                criteriaBuilder.currentTimestamp()
                                 );
        
        criteriaQuery.where(
                criteriaBuilder.between(criteriaBuilder.currentDate(),
                                        root.get(Pedido_.dataCriacao).as(java.sql.Date.class),
                                        joinPagamentoBoleto.get(PagamentoBoleto_.dataVencimento)
                                                           .as(java.sql.Date.class)),
                criteriaBuilder.equal(root.get(Pedido_.status), StatusPedido.AGUARDANDO)
                           );
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                r[0]
                + ", current_date: " + r[1]
                + ", current_time: " + r[2]
                + ", current_timestamp: " + r[3]));
    }
    
    @Test
    public void aplicarFuncaoNumero() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.abs(criteriaBuilder.prod(root.get(Pedido_.id), - 1)),
                criteriaBuilder.mod(root.get(Pedido_.id), 2),
                criteriaBuilder.sqrt(root.get(Pedido_.total))
                                 );
        
        criteriaQuery.where(
                criteriaBuilder.greaterThan(
                        criteriaBuilder.sqrt(root.get(Pedido_.total)), 10.00)
                           );
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                r[0]
                + ", abs: " + r[1]
                + ", mod: " + r[2]
                + ", sqrt: " + r[3]));
    }
    
    @Test
    public void aplicarFuncaoColecao() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                criteriaBuilder.size(root.get(Pedido_.itemPedidos))
                                 );
        
        criteriaQuery.where(
                criteriaBuilder.greaterThan(
                        criteriaBuilder.size(root.get(Pedido_.itemPedidos)), 1)
                           );
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                r[0]
                + ", size: " + r[1]
                                                 ));
    }
    
    @Test
    public void aplicarFuncaoNativas() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.multiselect(
                root.get(Pedido_.id),
                //varargs no ultimo parametro
                criteriaBuilder.function("dayname", String.class, root.get(Pedido_.dataCriacao))
                                 );
        
        criteriaQuery.where(criteriaBuilder.isTrue(
                criteriaBuilder.function("acima_media_faturamento", Boolean.class, root.get(Pedido_.total))));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                r[0]
                + ", dayname: " + r[1]
                                                 ));
    }
    
    @Test
    public void aplicarFuncaoAgregacao() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.multiselect(
                criteriaBuilder.avg(root.get(Pedido_.total)),
                criteriaBuilder.count(root.get(Pedido_.id)),
                criteriaBuilder.sum(root.get(Pedido_.total)),
                criteriaBuilder.min(root.get(Pedido_.total)),
                criteriaBuilder.max(root.get(Pedido_.total))
                                 );
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                "avg: " + r[0]
                + ", count: " + r[1]
                + ", sum: " + r[2]
                + ", min: " + r[3]
                + ", max: " + r[4]
                                                 ));
    }
}