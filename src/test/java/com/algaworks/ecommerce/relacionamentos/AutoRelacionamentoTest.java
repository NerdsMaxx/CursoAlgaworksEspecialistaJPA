package com.algaworks.ecommerce.relacionamentos;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AutoRelacionamentoTest extends EntityManagerTest {
    
    @Test
    public void verificarRelacionamento() {
        Categoria categoriaPai = new Categoria();
        categoriaPai.setNome("Eletrônicos");
        
        Categoria categoria = new Categoria();
        categoria.setNome("Celulares");
        categoria.setCategoriaPai(categoriaPai);
        
        entityManager.getTransaction().begin();
        entityManager.persist(categoriaPai);
        entityManager.persist(categoria);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Categoria categoriaVerificado = entityManager.find(Categoria.class, categoria.getId());
        assertNotNull(categoriaVerificado);
        assertNotNull(categoriaVerificado.getCategoriaPai());
        
        Categoria categoriaPaiVerificado = entityManager.find(Categoria.class, categoriaPai.getId());
        assertNotNull(categoriaPaiVerificado);
        assertEquals(1, categoriaPaiVerificado.getCategorias().size());
    }
}