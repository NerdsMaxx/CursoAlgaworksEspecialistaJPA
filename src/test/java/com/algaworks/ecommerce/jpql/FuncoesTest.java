package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FuncoesTest extends EntityManagerTest {
    
    @Test
    public void aplicarFuncao() {
        String jpql = "select c.id, concat('Categoria: ', c.nome) from Categoria c";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.nome, length(c.nome) from Categoria c";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.nome, length(c.nome) from Categoria c where length(c.nome) > 10";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.id, locate('a', c.nome) from Categoria c";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.id, substring(c.nome, 1, 3) from Categoria c";
        //A posição começa com 1, é o segundo argumento
        //Terceiro argumento é quantidade de caracteres a partir da posição escolhida
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.id, substring(c.nome, 1, 3) from Categoria c " +
               "where substring(c.nome, 1, 1) = 'N'";
        //A posição começa com 1, é o segundo argumento
        //Terceiro argumento é quantidade de caracteres a partir da posição escolhida
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.id, lower(c.nome) from Categoria c";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.id, upper(c.nome) from Categoria c";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
        
        
        jpql = "select c.id, trim(concat('  ',c.nome)) from Categoria c";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println(r[0] + " - " + r[1]));
    }
    
    @Test
    public void aplicarFuncaoData() {
        
        String jpql = "select current_date, current_time, current_timestamp from Pedido p where p.dataCriacao < current_timestamp";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
        
        
        
        
        
        jpql = "select day(p.dataCriacao), month(p.dataCriacao), year(p.dataCriacao) from Pedido p";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
        
        
        
        
        jpql = "select hour(p.dataCriacao), minute(p.dataCriacao), second(p.dataCriacao) from Pedido p where hour(p.dataCriacao) > 18";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
    }
    
    
    @Test
    public void aplicarFuncaoNumero() {
        String jpql = "select abs(-10), mod(3, 2), sqrt(9) from Pedido p";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
        
        
        
        
        jpql = "select abs(p.total), mod(p.id, 2), sqrt(p.total) from Pedido p where abs(p.total) > 499";
        
        typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
    }
    
    @Test
    public void aplicarFuncaoColecao() {
        String jpql = "select size(p.itemPedidos) from Pedido p where size(p.itemPedidos) > 1";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
    }
    
    
    @Test
    public void aplicarFuncaoNativa() {
        String jpql = "select p from Pedido p where function('acima_media_faturamento', p.total) = 1";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(p -> System.out.println(p.getId() + " - " + p.getTotal()));
        
        
        
        jpql = "select function('dayname', p.dataCriacao) from Pedido p where function('acima_media_faturamento', p.total) = 1";
        
        TypedQuery<Object[]> typedQuery1 = entityManager.createQuery(jpql, Object[].class);
        
        List<Object[]> resultado1 = typedQuery1.getResultList();
        
        assertFalse(resultado1.isEmpty());
        
        resultado1.stream().map(r -> Arrays.stream(r).map(Object::toString)
                                          .collect(joining(" | ")))
                 .forEach(System.out::println);
    }
    
    
    @Test
    public void aplicarFuncaoAgregacao() {
        String jpql = "select avg(p.total) from Pedido p";
        
        //está number pois funções diferentes de agragação retorna tipo numerico diferente (double, integer, ...)
        TypedQuery<Number> typedQuery = entityManager.createQuery(jpql, Number.class);
        
        List<Number> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(System.out::println);
        
        
        
        jpql = "select count(p.dataCriacao) from Pedido p";
        
        //está number pois funções diferentes de agragação retorna tipo numerico diferente (double, integer, ...)
        typedQuery = entityManager.createQuery(jpql, Number.class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(System.out::println);
        
        
        
        jpql = "select min(p.total) from Pedido p";
        
        //está number pois funções diferentes de agragação retorna tipo numerico diferente (double, integer, ...)
        typedQuery = entityManager.createQuery(jpql, Number.class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(System.out::println);
        
        
        
        jpql = "select max(p.total) from Pedido p";
        
        //está number pois funções diferentes de agragação retorna tipo numerico diferente (double, integer, ...)
        typedQuery = entityManager.createQuery(jpql, Number.class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(System.out::println);
        
        
        
        jpql = "select sum(p.total) from Pedido p";
        
        //está number pois funções diferentes de agragação retorna tipo numerico diferente (double, integer, ...)
        typedQuery = entityManager.createQuery(jpql, Number.class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(System.out::println);
    }
    
}