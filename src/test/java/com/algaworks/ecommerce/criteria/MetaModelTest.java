package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.Produto_;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MetaModelTest extends EntityManagerTest {

    @Test
    public void utilizarMetaModel() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        criteriaQuery.select(root);
        
//        criteriaQuery.where(criteriaBuilder.or(
//                criteriaBuilder.like(root.get(Produto_.nome), "%C%"),
//                criteriaBuilder.like(root.get(Produto_.descricao), "%C%")));
        criteriaQuery.where(criteriaBuilder.or(
                criteriaBuilder.like(root.get(Produto_.nome), "%K%"),
                criteriaBuilder.like(root.get(Produto_.descricao), "%K%")));
        
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println("id = " + r.getId() + ", nome = " + r.getNome() + ", descricao = " + r.getDescricao()));
    }

}