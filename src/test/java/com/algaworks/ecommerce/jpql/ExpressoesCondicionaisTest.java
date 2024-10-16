package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressoesCondicionaisTest extends EntityManagerTest {
    
    @Test
    public void usarExpressaoCondicionalLike() {
        String jpql = "select c from Cliente c where c.nome like concat(:nome, '%')";
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        typedQuery.setParameter("nome", "Fernando");
        
        List<Cliente> clientes = typedQuery.getResultList();
        
        assertEquals(1, clientes.size());
        
        
        
        jpql = "select c from Cliente c where c.nome like concat(:nome, '%')";
        
        typedQuery = entityManager.createQuery(jpql, Cliente.class);
        typedQuery.setParameter("nome", "Gabriel");
        
        clientes = typedQuery.getResultList();
        
        assertTrue(clientes.isEmpty());
        
        
        
        jpql = "select c from Cliente c where c.nome like concat('%', :nome)";
        
        typedQuery = entityManager.createQuery(jpql, Cliente.class);
        typedQuery.setParameter("nome", "Henrique");
        
        clientes = typedQuery.getResultList();
        
        assertEquals(1, clientes.size());
        
        
        
        jpql = "select c from Cliente c where c.nome like concat('%', :nome, '%')";
        
        typedQuery = entityManager.createQuery(jpql, Cliente.class);
        typedQuery.setParameter("nome", "e");
        
        clientes = typedQuery.getResultList();
        
        assertEquals(2, clientes.size());
    }
    
    @Test
    public void usarIsNull() {
        String jpql = "select p from Produto p where p.foto is null";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> produtos = typedQuery.getResultList();
        
        assertFalse(produtos.isEmpty());
        
        
        
        jpql = "select p from Produto p where p.foto is not null";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        produtos = typedQuery.getResultList();
        
        assertTrue(produtos.isEmpty());
    }
    
    @Test
    public void usarIsEmpty() {
        String jpql = "select p from Produto p where p.categorias is empty";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> produtos = typedQuery.getResultList();
        
        assertEquals(1, produtos.size());
        
        
        
        
        jpql = "select p from Produto p where p.categorias is not empty";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        produtos = typedQuery.getResultList();
        
        assertEquals(1, produtos.size());
    }
    
    @Test
    public void usarMaiorMenor() {
        String jpql = "select p from Produto p where p.preco > :preco";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("preco", BigDecimal.valueOf(499));
        
        List<Produto> produtos = typedQuery.getResultList();
        
        assertEquals(1, produtos.size());
        
        
        
        jpql = "select p from Produto p where p.preco >= :preco";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("preco", BigDecimal.valueOf(499));
        
        produtos = typedQuery.getResultList();
        
        assertEquals(2, produtos.size());
        
        
        
        jpql = "select p from Produto p where p.preco < :preco";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("preco", BigDecimal.valueOf(499));
        
        produtos = typedQuery.getResultList();
        
        assertTrue(produtos.isEmpty());
        
        
        
        jpql = "select p from Produto p where p.preco <= :preco";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("preco", BigDecimal.valueOf(499));
        
        produtos = typedQuery.getResultList();
        
        assertEquals(1, produtos.size());
        
        
        
        jpql = "select p from Produto p where p.preco >= :precoInicial and p.preco <= :precoFinal";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("precoInicial", BigDecimal.valueOf(499));
        typedQuery.setParameter("precoFinal", BigDecimal.valueOf(1500));
        
        produtos = typedQuery.getResultList();
        
        assertEquals(2, produtos.size());
    }
    
    @Test
    public void usarMaiorMenorComDatas() {
        String jpql = "select p from Pedido p where p.dataCriacao > :data";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        typedQuery.setParameter("data", LocalDateTime.now().minusDays(2));
        
        List<Pedido> pedidos = typedQuery.getResultList();
        assertFalse(pedidos.isEmpty());
    }
    
    @Test
    public void usarBetween() {
        String jpql = "select p from Produto p " +
                      "where p.preco between :precoInicial and :precoFinal";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("precoInicial", BigDecimal.valueOf(499));
        typedQuery.setParameter("precoFinal", BigDecimal.valueOf(1500));
        
        List<Produto> produtos = typedQuery.getResultList();
        
        assertEquals(2, produtos.size());
        
        
        
        
        jpql = "select p from Produto p " +
                      "where p.dataCriacao between :dataInicial and :dataFinal";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        typedQuery.setParameter("dataInicial", LocalDateTime.now().minusDays(4));
        typedQuery.setParameter("dataFinal", LocalDateTime.now());
        
        produtos = typedQuery.getResultList();
        
        assertEquals(1, produtos.size());
    }
    
    @Test
    public void usarExpressaoDiferente() {
        String jpql = "select p from Produto p where p.preco <> 100";
//        String jpql = "select p from Produto p where p.preco != 100";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> produtos = typedQuery.getResultList();
        assertFalse(produtos.isEmpty());
    }
}