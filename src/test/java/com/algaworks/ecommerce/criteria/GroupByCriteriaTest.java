package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GroupByCriteriaTest extends EntityManagerTest {
    
    @Test
    public void agruparResultado01() {
//        Quantidade de produtos por categoria.
//        String jpql = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Categoria> root = criteriaQuery.from(Categoria.class);
        Join<Categoria, Produto> joinProduto = root.join(Categoria_.produtos, JoinType.LEFT);
        
        criteriaQuery.multiselect(
                root.get(Categoria_.nome),
                criteriaBuilder.count(joinProduto.get(Produto_.id))
                                 );
        
        criteriaQuery.groupBy(root.get(Categoria_.id));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                "nome: " + r[0]
                + ", count: " + r[1]));
    }
    
    @Test
    public void agruparResultado02() {
//        Total de vendas por categoria.
//        String jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip " +
//                " join ip.produto pro join pro.categorias c " +
//                " group by c.id";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinCategoria = joinProduto.join(Produto_.categorias);
        
        criteriaQuery.multiselect(
                joinCategoria.get(Categoria_.nome),
                criteriaBuilder.sum(root.get(ItemPedido_.precoProduto))
                                 );
        
        criteriaQuery.groupBy(joinCategoria.get(Categoria_.id));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(
                "nome: " + r[0]
                + ", sum: " + r[1]));
    }
    
    @Test
    public void agruparResultadoExercicio() {
//        Total de vendas por cliente
//        String jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip " +
//                " join ip.pedido p join p.cliente c " +
//                " group by c.id";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Pedido> joinPedido = root.join(ItemPedido_.pedido);
        Join<Pedido, Cliente> joinPedidoCliente = joinPedido.join(Pedido_.cliente);
        
        criteriaQuery.multiselect(
                joinPedidoCliente.get(Cliente_.nome),
                criteriaBuilder.sum(root.get(ItemPedido_.precoProduto))
                                 );
        
        criteriaQuery.groupBy(joinPedidoCliente.get(Cliente_.id));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> resultado = typedQuery.getResultList();
        
        resultado.forEach(r -> System.out.println("Nome cliente: " + r[0] + ", Sum: " + r[1]));
    }
    
    @Test
    public void agruparResultadoComFuncoes() {
//         Total de vendas por mês.
//        String jpql = "select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total) " +
//                " from Pedido p " +
//                " group by year(p.dataCriacao), month(p.dataCriacao) ";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        //year, month, monthname são funções nativas do MySQL, não são da JPA
        Expression<Integer> anoCriacaoPedido = criteriaBuilder
                .function("year", Integer.class, root.get(Pedido_.dataCriacao));
        
        Expression<Integer> mesCriacaoPedido = criteriaBuilder
                .function("month", Integer.class, root.get(Pedido_.dataCriacao));
        
        Expression<String> nomeMesCriacaoPedido = criteriaBuilder
                .function("monthname", String.class, root.get(Pedido_.dataCriacao));
        
        Expression<String> anoMesConcat = criteriaBuilder.concat(
                criteriaBuilder.concat(anoCriacaoPedido.as(String.class), "/"),
                nomeMesCriacaoPedido);
        
        criteriaQuery.multiselect(
                anoMesConcat,
                criteriaBuilder.sum(root.get(Pedido_.total))
                                 );
        
        criteriaQuery.groupBy(anoMesConcat, anoCriacaoPedido, mesCriacaoPedido);
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> resultado = typedQuery.getResultList();
        
        resultado.forEach(r -> System.out.println("Ano/Mês: " + r[0] + ", Sum: " + r[1]));
    }
    
    @Test
    public void condicionarAgrupamentoComHaving() {
//         Total de vendas dentre as categorias que mais vendem.
//        String jpql = "select cat.nome, sum(ip.precoProduto) from ItemPedido ip " +
//                " join ip.produto pro join pro.categorias cat " +
//                " group by cat.id " +
//                " having sum(ip.precoProduto) > 100 ";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = root.join(ItemPedido_.produto);
        Join<Produto, Categoria> joinProdutoCategoria = joinProduto.join(Produto_.categorias);
        
        criteriaQuery.multiselect(
                joinProdutoCategoria.get(Categoria_.nome),
                criteriaBuilder.sum(root.get(ItemPedido_.precoProduto)),
                criteriaBuilder.avg(root.get(ItemPedido_.precoProduto))
                                 );
        
        criteriaQuery.groupBy(joinProdutoCategoria.get(Categoria_.id));
        
        criteriaQuery.having(criteriaBuilder.greaterThan(
                criteriaBuilder.avg(
                        root.get(ItemPedido_.precoProduto)).as(BigDecimal.class),
                new BigDecimal(700)));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> resultado = typedQuery.getResultList();
        
        resultado.forEach(r -> System.out.println(
                "Nome: " + r[0]
                + ", sum: " + r[1]
                + ", avg: " + r[2]));
    }
    
}