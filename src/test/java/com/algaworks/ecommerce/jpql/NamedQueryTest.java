package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class NamedQueryTest extends EntityManagerTest {

    @Test
    public void executarConsulta() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.listar", Produto.class);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        
        
        typedQuery = entityManager.createNamedQuery("Produto.listarPorCategoria", Produto.class);
        typedQuery.setParameter("categoria", 9);
        
        resultado = typedQuery.getResultList();
        resultado.forEach(p -> System.out.println(p.getId()));
        
        assertFalse(resultado.isEmpty());
    }
    
    @Test
    public void executarConsultaArquivoXML() {
        TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("Pedido.listar", Pedido.class);
        
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
    }
    
    @Test
    public void executarConsultaArquivoXMLEspecifico() {
        TypedQuery<Pedido> typedQuery = entityManager.createNamedQuery("Pedido.todos", Pedido.class);
        
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
    }
    
    @Test
    public void executarConsultaArquivoXMLEspecificoProduto() {
        TypedQuery<Produto> typedQuery = entityManager.createNamedQuery("Produto.todos", Produto.class);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
    }
}