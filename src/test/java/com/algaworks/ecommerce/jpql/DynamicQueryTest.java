package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicQueryTest extends EntityManagerTest {
    
    @Test
    public void executarConsultaDinamica() {
        Produto consultado = new Produto();
        consultado.setNome("Samsung");
        
        List<Produto> resultado = pesquisar(consultado);
        
        assertFalse(resultado.isEmpty());
        assertEquals("Samsung Galaxy S24+", resultado.get(0).getNome());
        assertEquals("Samsung Galaxy S25+", resultado.get(1).getNome());
        assertEquals("Samsung Galaxy S26+", resultado.get(2).getNome());
    }
    
    private List<Produto> pesquisar(Produto consultado) {
        StringBuilder jpql = new StringBuilder("select p from Produto p where 1 = 1");
        
        if (consultado.getNome() != null) {
            jpql.append(" and p.nome like concat('%', :nome, '%')");
        }
        
        if (consultado.getDescricao() != null) {
            jpql.append(" and p.descricao like concat('%', :descricao, '%')");
        }
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql.toString(), Produto.class);
        
        if (consultado.getNome() != null) {
            typedQuery.setParameter("nome", consultado.getNome());
        }
        
        if (consultado.getDescricao() != null) {
            typedQuery.setParameter("descricao", consultado.getDescricao());
        }
        
        return typedQuery.getResultList();
    }
}