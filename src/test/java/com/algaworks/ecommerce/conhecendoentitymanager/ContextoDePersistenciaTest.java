package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ContextoDePersistenciaTest extends EntityManagerTest {
    
    @Test
    public void usarContextoPersistencia() {
        Produto produto = entityManager.find(Produto.class, 1);
        
        entityManager.getTransaction().begin();
        
        produto.setPreco(BigDecimal.valueOf(100.00));
        
        Produto produto2 = new Produto();
        produto2.setNome("Caneca para café");
        produto2.setPreco(BigDecimal.valueOf(10.00));
        produto2.setDescricao("Boa caneca para café");
        produto2.setDataCriacao(LocalDateTime.now());
        entityManager.persist(produto2);
        
        Produto produto3 = new Produto();
        produto3.setNome("Caneca para chá");
        produto3.setPreco(BigDecimal.valueOf(10.00));
        produto3.setDescricao("Boa caneca para chá");
        produto3.setDataCriacao(LocalDateTime.now());
        produto3 = entityManager.merge(produto3);
        
        produto3.setDescricao("Alterar descrição");
        
        entityManager.getTransaction().commit();
    }
}