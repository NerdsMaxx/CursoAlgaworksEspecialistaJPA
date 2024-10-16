package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FuncoesStringTest extends EntityManagerTest {
    
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
}