package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class DetalhesColumnTest extends EntityManagerTest {
    
    @Test
    public void impedirInsercaoDaColunaAtualizacao() {
        Produto produto = new Produto();
        produto.setNome("Teclado para smartphone");
        produto.setDescricao("O mais confortável");
        produto.setPreco(BigDecimal.valueOf(500.00));
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataUltimaAtualizacao(LocalDateTime.now());
        
        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertNotNull(produtoVerificado.getDataCriacao());
        assertNull(produtoVerificado.getDataUltimaAtualizacao());
    }
    
    @Test
    public void impedirAtualizacaoDaColunaCriacao() {
        entityManager.getTransaction().begin();
        
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setPreco(BigDecimal.TEN);
        produto.setDataCriacao(LocalDateTime.now());
        produto.setDataUltimaAtualizacao(LocalDateTime.now());
        
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        
        assertNotEquals(produto.getDataCriacao().truncatedTo(ChronoUnit.SECONDS),
                        produtoVerificado.getDataCriacao().truncatedTo(ChronoUnit.SECONDS));
        
        assertEquals(produto.getDataUltimaAtualizacao().truncatedTo(ChronoUnit.SECONDS),
                     produtoVerificado.getDataUltimaAtualizacao().truncatedTo(ChronoUnit.SECONDS));
    }
}