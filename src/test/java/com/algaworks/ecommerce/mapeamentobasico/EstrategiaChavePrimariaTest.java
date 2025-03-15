package com.algaworks.ecommerce.mapeamentobasico;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EstrategiaChavePrimariaTest extends EntityManagerTest {

    @Test
    public void testarEstrategiaChave() {
        //Categoria
        Categoria categoria = new Categoria();
        
        categoria.setNome("LIVRO");
        
        entityManager.getTransaction().begin();
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Categoria categoriaVerificado = entityManager.find(Categoria.class, categoria.getId());
        assertNotNull(categoriaVerificado);
        
        
        //Produto
//        Produto produto = new Produto();
//
//        produto.setNome("Notebook Acer");
//        produto.setDescricao("Sei lá");
//        produto.setPreco(BigDecimal.valueOf(5000.00));
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(produto);
//        entityManager.getTransaction().commit();
//
//        entityManager.clear();
//
//        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
//        assertNotNull(categoriaVerificado);
    }
}