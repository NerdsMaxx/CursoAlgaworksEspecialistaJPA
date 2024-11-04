package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Pedido_;
import com.algaworks.ecommerce.model.StatusPedido;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class OperadoresLogicosCriteriaTest extends EntityManagerTest {
    
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    @Test
    public void usarOperadores() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        //SELECT p FROM Pedido p WHERE (p.total != 499 AND (p.status = 'PAGO' OR p.status = 'CANCELADO')) AND p.dataCriacao > {dataAtualMenos5Dias}
        criteriaQuery.where(//no método and, tu pode colocar quantas condições tu quiser, é um vararg
                criteriaBuilder.and(criteriaBuilder.notEqual(root.get(Pedido_.total), new BigDecimal("499")),
                                    //no método or, tu pode colocar quantas condições tu quiser, é um vararg
                                    criteriaBuilder.or(
                                            criteriaBuilder.equal(root.get(Pedido_.status), StatusPedido.PAGO),
                                            criteriaBuilder.equal(root.get(Pedido_.status), StatusPedido.CANCELADO)),
                criteriaBuilder.greaterThan(root.get(Pedido_.dataCriacao),
                                            LocalDateTime.now().minusDays(5))
                ));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        resultado.forEach(r -> System.out.println("id = " + r.getId() + ", total = " + r.getTotal() + ", dataCriacao = " + dtf.format(r.getDataCriacao()) + ", statusPagamento = " + r.getStatus()));
    }
}