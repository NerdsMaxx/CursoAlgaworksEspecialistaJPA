package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Categoria;
import org.junit.jupiter.api.Test;

public class EstadosECicloDeVidaTest extends EntityManagerTest {
    
    @Test
    public void analisarEstado() {
        Categoria categoriaNovo = new Categoria();
        categoriaNovo.setNome("Eletrônico");
        
        Categoria categoriaGerenciadaMerge = entityManager.merge(categoriaNovo);
        Categoria categoriaGerenciada = entityManager.find(Categoria.class, 1);
        
        entityManager.remove(categoriaGerenciada);
        entityManager.persist(categoriaGerenciada);
        
        entityManager.detach(categoriaGerenciada);
    }
}