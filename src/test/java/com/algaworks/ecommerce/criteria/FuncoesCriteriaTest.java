package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Pedido_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
        
        criteriaQuery.multiselect(root.get(Pedido_.id),
                                  criteriaBuilder.currentDate(),
                                  criteriaBuilder.currentTime(),
                                  criteriaBuilder.currentTimestamp());
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.stream().limit(1)
                 .forEach(r -> System.out.println(
                "currentDate: " + r[1]
                + "\ncurrentTime: " + r[2]
                + "\ncurrentTimestamp: " + r[3]
                + "\n\n\n"));
    }
}