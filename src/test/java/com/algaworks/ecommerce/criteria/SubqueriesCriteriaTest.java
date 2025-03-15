package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SubqueriesCriteriaTest extends EntityManagerTest {
    
    @Test
    public void pesquisarSubqueries01() {
//        String jpql = "select p from Produto p " +
//                      "where p.preco = " +
//                      "(select max(p.preco) from Produto p)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Produto> subqueryRoot = subquery.from(Produto.class);
        subquery.select(criteriaBuilder.max(subqueryRoot.get(Produto_.preco)));
        
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(
                root.get(Produto_.preco), subquery));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(p -> out.println(
                "Produto Id: " + p.getId() +
                ", Nome: " + p.getNome() +
                ", Preco: " + p.getPreco()));
    }
    
    @Test
    public void pesquisarSubqueries02() {
//        String jpql = "select p from Pedido p " +
//        "where p.total > (select avg(p.total) " +
//        "from Pedido p)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.avg(subqueryRoot.get(Pedido_.total))
                                       .as(BigDecimal.class));
        
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.greaterThan(
                root.get(Pedido_.total), subquery));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(p -> out.println(
                "Pedido Id: " + p.getId() +
                ", Total: " + p.getTotal()));
    }
    
    @Test
    public void pesquisarSubqueries03() {
//        Bons clientes
//        String jpql = "select c from Cliente c where " +
//                      "(select sum(p.total) from Pedido p where p.cliente = c) >= 1300";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.sum(subqueryRoot.get(Pedido_.total)));
        subquery.where(criteriaBuilder.equal(
                subqueryRoot.get(Pedido_.cliente), root));
        
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(
                subquery, new BigDecimal("1300.00")));
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Cliente> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println(
                "Cliente Id: " + r.getId() +
                ", Nome: " + r.getNome()));
    }
    
    @Test
    public void pesquisarSubqueriesComIN() {
//        String jpql = "select ped from Pedido ped " +
//                      "where ped.id in (select ped2.id from ItemPedido ip " +
//                      "join ip.pedido ped2 " +
//                      "join ip.produto pro2 " +
//                      "where pro2.preco > 100)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        Subquery<Pedido> subquery = criteriaQuery.subquery(Pedido.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Pedido> joinPedido = subqueryRoot.join(ItemPedido_.pedido);
        Join<ItemPedido, Produto> joinProduto = subqueryRoot.join(ItemPedido_.produto);
        
        subquery.select(joinPedido);
        subquery.where(criteriaBuilder.greaterThan(
                joinProduto.get(Produto_.preco), new BigDecimal("100.00")));
        
        
        criteriaQuery.select(root);
        criteriaQuery.where(root.in(subquery));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("ID: " + r.getId()));
    }
    
    @Test
    public void pesquisarSubqueriesComExists() {
//        String jpql = "select pro from Produto pro " +
//                      "where exists " +
//                      "(select 1 from ItemPedido ip2 " +
//                      "join ip2.produto pro2 " +
//                      "where pro2 = pro)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Produto> joinProduto = subqueryRoot.join(ItemPedido_.produto);
        
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(criteriaBuilder.equal(
                joinProduto, root));
//        subquery.where(criteriaBuilder.equal(
//                subqueryRoot.get(ItemPedido_.produto), root));
        
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.exists(subquery));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(p -> out.println(
                "Produto Id: " + p.getId() +
                ", Nome: " + p.getNome() +
                ", Preco: " + p.getPreco()));
    }
    
    @Test
    public void perquisarComSubqueryExercicio() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);
        
        criteriaQuery.select(root);
        
        Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
        Root<Pedido> subqueryRoot = subquery.from(Pedido.class);
        subquery.select(criteriaBuilder.count(criteriaBuilder.literal(1)));
        subquery.where(criteriaBuilder.equal(subqueryRoot.get(Pedido_.cliente), root));
        
        criteriaQuery.where(criteriaBuilder.greaterThan(subquery, 2L));
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Cliente> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println(
                "Cliente Id: " + r.getId() +
                ", Nome: " + r.getNome()));
    }
    
    @Test
    public void pesquisarComINExercicio() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.select(root);
        
        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        Join<ItemPedido, Produto> subqueryJoinProduto = subqueryRoot.join(ItemPedido_.produto);
        Join<Produto, Categoria> subqueryJoinProdutoCategoria = subqueryJoinProduto
                .join(Produto_.categorias);
        subquery.select(subqueryRoot.get(ItemPedido_.id).get(ItemPedidoId_.pedidoId));
        subquery.where(criteriaBuilder.equal(subqueryJoinProdutoCategoria.get(Categoria_.id), 2));
        
        criteriaQuery.where(root.get(Pedido_.id).in(subquery));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("ID: " + r.getId()));
    }
    
    @Test
    public void perquisarComExistsExercicio() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        criteriaQuery.select(root);
        
        Subquery<Integer> subquery = criteriaQuery.subquery(Integer.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(
                criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root),
                criteriaBuilder.notEqual(
                        subqueryRoot.get(ItemPedido_.precoProduto), root.get(Produto_.preco))
                      );
        
        criteriaQuery.where(criteriaBuilder.exists(subquery));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("ID: " + r.getId()));
    }
    
    @Test
    public void pesquisarComAll01() {
//        String jpql = "select p from Produto p " +
//                      "where p.preco  = ALL (select precoProduto " +
//                      "from ItemPedido ip " +
//                      "where ip.produto = p)";
    
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(
                root, subqueryRoot.get(ItemPedido_.produto)));
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.equal(
                root.get(Produto_.preco), criteriaBuilder.all(subquery)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId() +
                                           ", nome = " + r.getNome() +
                                           ", preco = " + r.getPreco()));
    }
    
    @Test
    public void pesquisarComAll02() {
//        String jpql = "select p from Produto p " +
//                      "where p.preco  > ALL (select precoProduto " +
//                      "from ItemPedido ip " +
//                      "where ip.produto = p)
//                      and exists (select 1
//                      from ItemPedido ip1
//                      where ip1.produto = p)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        //PODE UTILIZAR A MESMA SUBQUERY PARA EXISTS
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(
                root, subqueryRoot.get(ItemPedido_.produto)));
        
        //OUTRA ALTERNATIVA, MAS QUE NÃO PRECISAVA, DEVOLVE LITERAL 1 OU NÃO,
        // NÃO FAZ DIFERENÇA PARA EXISTS
//        Subquery<Integer> subqueryExists = criteriaQuery.subquery(Integer.class);
//        Root<ItemPedido> subqueryExistsRoot = subqueryExists.from(ItemPedido.class);
//
//        subqueryExists.select(criteriaBuilder.literal(1));
//        subqueryExists.where(criteriaBuilder.equal(
//                subqueryExistsRoot.get(ItemPedido_.produto), root));
        
        criteriaQuery.select(root);
        
//        criteriaQuery.where(criteriaBuilder.greaterThan(
//                root.get(Produto_.preco), criteriaBuilder.all(subquery)),
//                            criteriaBuilder.exists(subqueryExists));
        
        criteriaQuery.where(criteriaBuilder.greaterThan(
                root.get(Produto_.preco), criteriaBuilder.all(subquery)),
                            criteriaBuilder.exists(subquery));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId() +
                                           ", nome = " + r.getNome() +
                                           ", preco = " + r.getPreco()));
    }
    
    @Test
    public void pesquisarComAnyEquals() {
//        String jpql = "select p from Produto p " +
//                      "where p.preco  = ANY (" +
//                      "select precoProduto from ItemPedido " +
//                      "where produto = p)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(
                root, subqueryRoot.get(ItemPedido_.produto)));
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.equal(
                root.get(Produto_.preco), criteriaBuilder.any(subquery)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId() +
                                           ", nome = " + r.getNome() +
                                           ", preco = " + r.getPreco()));
    }
    
    @Test
    public void pesquisarComAnyNotEquals() {
//        String jpql = "select p from Produto p " +
////                    "where p.preco != ANY (" +
////                    "select precoProduto from ItemPedido " +
////                    "where produto = p)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(criteriaBuilder.equal(
                root, subqueryRoot.get(ItemPedido_.produto)));
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.notEqual(
                root.get(Produto_.preco), criteriaBuilder.any(subquery)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId() +
                                           ", nome = " + r.getNome() +
                                           ", preco = " + r.getPreco()));
    }
    
    @Test
    public void pesquisarComAllExercicio() {
//        Todos os produtos que sempre foram vendidos pelo mesmo preço.
//        String jpql = "select distinct p from ItemPedido ip join ip.produto p where " +
//                " ip.precoProduto = ALL " +
//                " (select precoProduto from ItemPedido where produto = p and id <> ip.id)";
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<ItemPedido> root = criteriaQuery.from(ItemPedido.class);
        
        criteriaQuery.select(root.get(ItemPedido_.produto));
        criteriaQuery.distinct(true);
        
        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<ItemPedido> subqueryRoot = subquery.from(ItemPedido.class);
        subquery.select(subqueryRoot.get(ItemPedido_.precoProduto));
        subquery.where(
                criteriaBuilder.equal(subqueryRoot.get(ItemPedido_.produto), root.get(ItemPedido_.produto)),
                criteriaBuilder.notEqual(subqueryRoot, root)
                      );
        
        criteriaQuery.where(
                criteriaBuilder.equal(
                        root.get(ItemPedido_.precoProduto), criteriaBuilder.all(subquery))
                           );
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId() +
                                           ", nome = " + r.getNome() +
                                           ", preco = " + r.getPreco()));
    }
}