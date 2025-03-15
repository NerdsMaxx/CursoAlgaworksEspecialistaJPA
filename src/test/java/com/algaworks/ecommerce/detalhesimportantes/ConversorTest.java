package com.algaworks.ecommerce.detalhesimportantes;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConversorTest extends EntityManagerTest {
    
    //Converter pode ser interessante para trabalhar com BD legado como por exemplo SIM ou NAO, mas no Java pode ser Boolean normal.
    @Test
    public void converter() {
        Produto produto = new Produto();
        produto.setDataCriacao(LocalDateTime.now());
        produto.setNome("Carregador de Notebook Dell");
        produto.setAtivo(true);
//        produto.setAtivo(false);
        
        entityManager.getTransaction().begin();
        
        entityManager.persist(produto);
        
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertTrue(produtoVerificado.getAtivo());
//        assertFalse(produtoVerificado.getAtivo());
    }
}