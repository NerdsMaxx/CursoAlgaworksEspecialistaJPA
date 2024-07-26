package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultandoRegistrosTest extends EntityManagerTest {
    
    @Test
    public void deveriaBuscarProdutoComSucessoPorId() {
//        Produto produto = entityManager.getReference(Produto.class, 1); // getReference consegue ser lazy
//        System.out.println("Ainda não buscou!");
        Produto produto = entityManager.find(Produto.class, 1); // find retorna direto
        System.out.println("Buscou!");
        assertNotNull(produto);
        assertEquals("Kindle", produto.getNome());
        assertEquals("Conheça o novo Kindle!", produto.getDescricao());
        assertEquals(new BigDecimal("499.00"), produto.getPreco());
    }
    
    @Test
    public void deveriaBuscarProdutoInexistentePorId() {
        Produto produto = entityManager.find(Produto.class, 1000);
        assertNull(produto);
    }
    
    @Test
    public void deveriaVoltarAosValoresIniciaisDoProduto() {
        Produto produto = entityManager.find(Produto.class, 1);
        produto.setNome("Microfone Samson");
        
        entityManager.refresh(produto);
        
        assertEquals("Kindle", produto.getNome());
    }
}