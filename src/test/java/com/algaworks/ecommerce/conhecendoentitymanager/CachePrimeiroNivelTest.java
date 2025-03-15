package com.algaworks.ecommerce.conhecendoentitymanager;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;

public class CachePrimeiroNivelTest extends EntityManagerTest {
    
    @Test
    public void verificarCache() {
        Produto produto = entityManager.find(Produto.class, 1);
        out.println(produto);
        out.println("=".repeat(15));
//        entityManager.clear(); //Vai limpar cache e vai forçar pegar a entidade denovo no BD
        
        Produto produtoRegastado = entityManager.find(Produto.class, produto.getId()); //Vai pegar a mesma entidade que já estava na memória, não vai recriar outra entidade com mesmo conteúdo, por isso produto == produtoRegastado dá true.
        
        out.println(produtoRegastado);
        
        Assertions.assertTrue(produto == produtoRegastado);
    }
}