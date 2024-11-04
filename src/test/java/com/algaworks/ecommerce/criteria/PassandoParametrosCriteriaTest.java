package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.NotaFiscal;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.TemporalType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PassandoParametrosCriteriaTest extends EntityManagerTest {
    
    @Test
    public void passarParametro() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
//        ParameterExpression<Integer> parameterExpressionId = criteriaBuilder.parameter(Integer.class);
        ParameterExpression<Integer> parameterExpressionId = criteriaBuilder.parameter(Integer.class, "id");
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), parameterExpressionId));
        
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
//        typedQuery.setParameter(parameterExpressionId, 1);
        typedQuery.setParameter("id", 1);
        
        Pedido pedido = typedQuery.getSingleResult();
        
        assertNotNull(pedido);
    }
    
    @Test
    public void passarParametroNotaDate() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<NotaFiscal> criteriaQuery = criteriaBuilder.createQuery(NotaFiscal.class);
        Root<NotaFiscal> root = criteriaQuery.from(NotaFiscal.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);

//        ParameterExpression<Integer> parameterExpressionId = criteriaBuilder.parameter(Integer.class);
        ParameterExpression<Date> parameterExpressionDate = criteriaBuilder.parameter(Date.class, "dataInicial");
        
        criteriaQuery.where(criteriaBuilder.greaterThan(root.get("dataEmissao"), parameterExpressionDate));
        
        
        TypedQuery<NotaFiscal> typedQuery = entityManager.createQuery(criteriaQuery);
        
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -30);
        Date dataInicial = c.getTime();
        
//        typedQuery.setParameter(parameterExpressionId, 1);
        typedQuery.setParameter("dataInicial", dataInicial, TemporalType.TIMESTAMP);
        
        List<NotaFiscal> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
    }
}