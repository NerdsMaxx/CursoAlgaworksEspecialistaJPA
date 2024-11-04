package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GroupByTest extends EntityManagerTest {
    
    @Test
    public void agruparResultado() {
        //Quantidade de produtos por categoria
        String jpql = "select c.nome, count(p.id) from Categoria c join c.produtos p group by c.id";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
        
        
        //Total de vendas por mês
        jpql = "select concat(year(p.dataCriacao), ' ', function('monthname', p.dataCriacao)), " +
               "sum(p.total) from Pedido p " +
               "group by year(p.dataCriacao), month(p.dataCriacao)";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
        
        
        //Total de vendas por categoria
        jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip join ip.produto p join p.categorias c group by c.id";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
    }
    
    
    
    @Test
    public void agruparEFiltrarResultado() {
//         Total de vendas por mês.
//        String jpql = "select concat(year(p.dataCriacao), '/', function('monthname', p.dataCriacao)), sum(p.total) " +
//                " from Pedido p " +
//                " where year(p.dataCriacao) = year(current_date) " +
//                " group by year(p.dataCriacao), month(p.dataCriacao) ";

//         Total de vendas por categoria.
//        String jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip " +
//                " join ip.produto pro join pro.categorias c join ip.pedido p " +
//                " where year(p.dataCriacao) = year(current_date) and month(p.dataCriacao) = month(current_date) " +
//                " group by c.id";

//        Total de vendas por cliente
        String jpql = "select c.nome, sum(ip.precoProduto) from ItemPedido ip " +
                      " join ip.pedido p join p.cliente c " +
                      " where year(p.dataCriacao) = year(current_date) and month(p.dataCriacao) >= (month(current_date) - 3) " +
                      " group by c.id";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
    }
    
    @Test
    public void condicionarAgrupamentoComHaving() {
        // Total de vendas dentre as categorias que mais vendem acima de X reais
        String jpql = "select cat.nome, sum(ip.precoProduto) " +
                      "from ItemPedido ip " +
                      "join ip.produto pro " +
                      "join pro.categorias cat " +
                      "group by cat.id " +
                      "having sum(ip.precoProduto) > 900";
        //pode ter avg e outras funções
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
        
        
        
        jpql = "select cat.id, cat.nome, sum(ip.precoProduto) " +
               "from ItemPedido ip " +
               "join ip.produto pro " +
               "join pro.categorias cat " +
               "group by cat.id " +
               "having cat.id > 2";
        //EMBORA FUNCIONE cat.id > 2, NÃO FAZ MUITO SENTIDO USAR NO HAVING
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
        
        
        
        jpql = "select cat.nome, sum(ip.precoProduto) " +
               "from ItemPedido ip " +
               "join ip.produto pro " +
               "join pro.categorias cat " +
               "group by cat.nome " +
               "having cat.id > 2";
        //COMO AGORA VAI AGRUPAR POR nome, NÃO VAI FUNCIONAR having cat.id > 2 e SELECT NÃO COMEÇA COM cat.id E SIM COM cat.nome, POR ISSO VAI DAR ERRO!
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" - ")))
                 .forEach(System.out::println);
     }
}