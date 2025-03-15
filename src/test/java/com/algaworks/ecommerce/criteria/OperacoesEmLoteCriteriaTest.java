package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Categoria_;
import com.algaworks.ecommerce.model.Produto;
import com.algaworks.ecommerce.model.Produto_;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class OperacoesEmLoteCriteriaTest extends EntityManagerTest {
    
    @Test
    public void atualizarEmLote() {
    
//        String jpql = "update Produto p set p.preco = p.preco + (p.preco * 0.1) " +
//                      " where exists (select 1 from p.categorias c2 where c2.id = :categoria)";
        
        entityManager.getTransaction().begin();
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Produto> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Produto.class);
        Root<Produto> root = criteriaUpdate.from(Produto.class);
        
        criteriaUpdate.set(
                root.get(Produto_.preco),
                criteriaBuilder.prod(root.get(Produto_.preco),
                                     new BigDecimal("1.1")));
        
        Subquery<Integer> subquery = criteriaUpdate.subquery(Integer.class);
        
        //correlate tmb funciona para criteriaQuery
        Root<Produto> subqueryRoot = subquery.correlate(root);
        Join<Produto,Categoria> joinCategoria = subqueryRoot.join(Produto_.categorias);
        
        subquery.select(criteriaBuilder.literal(1));
        subquery.where(criteriaBuilder.equal(joinCategoria.get(Categoria_.id), 2));
        
        criteriaUpdate.where(criteriaBuilder.exists(subquery));
        
        Query query = entityManager.createQuery(criteriaUpdate);
        query.executeUpdate();
        
        entityManager.getTransaction().commit();
    }
    
    @Test
    public void deletarEmLote() {

//        String jpql = "delete from Produto p where p.id between 5 and 12";
        
        entityManager.getTransaction().begin();
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<Produto> criteriaDelete = criteriaBuilder.createCriteriaDelete(Produto.class);
        Root<Produto> root = criteriaDelete.from(Produto.class);
        
        criteriaDelete.where(
                criteriaBuilder.between(root.get(Produto_.id), 5, 12));
        
        Query query = entityManager.createQuery(criteriaDelete);
        query.executeUpdate();
        
        entityManager.getTransaction().commit();
    }
}