package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class RelacionamentoManyToManyTest extends EntityManagerTest {
    
    @Test
    public void naoDeveriaSalvarProdutosPelaCategoria() {
        Produto produto = entityManager.find(Produto.class, 1);
        Categoria categoria = entityManager.find(Categoria.class, 1);
        
        entityManager.getTransaction().begin();
        categoria.setProdutos(Collections.singletonList(produto));
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Categoria categoriaVerificado = entityManager.find(Categoria.class, categoria.getId());
        assertTrue(categoriaVerificado.getProdutos().isEmpty());
    }
    
    @Test
    public void deveriaSalvarCategoriaPeloProduto() {
        Produto produto = entityManager.find(Produto.class, 1);
        Categoria categoria = entityManager.find(Categoria.class, 1);
        
        entityManager.getTransaction().begin();
        produto.setCategorias(Collections.singletonList(categoria));
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Categoria categoriaVerificado = entityManager.find(Categoria.class, categoria.getId());
        assertEquals(1, categoriaVerificado.getProdutos().size());
    }
}