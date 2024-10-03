package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OperacoesComTransacaoTest extends EntityManagerTest {
    
    @Test
    public void inserirOPrimeiroObjeto() {
        Produto novoProduto = new Produto();
        
//        novoProduto.setId(2);
        novoProduto.setNome("Câmera Canon");
        novoProduto.setDescricao("A melhor definição para suas fotos.");
        novoProduto.setPreco(BigDecimal.valueOf(5000));
        novoProduto.setDataCriacao(LocalDateTime.now());
        novoProduto.setTags(List.of("Foto"));
        
        entityManager.getTransaction().begin();
        entityManager.persist(novoProduto);
//        entityManager.flush();
        entityManager.getTransaction().commit();
        
//        entityManager.clear();
        
        Produto produtoVerificacao = entityManager.find(Produto.class, novoProduto.getId());
        assertNotNull(produtoVerificacao);
    }
    
    @Test
    public void inserirObjetoComMerge() {
        Produto novoProduto = new Produto();
        
//        novoProduto.setId(4);
        novoProduto.setNome("Câmera Canon 2");
        novoProduto.setDescricao("A melhor definição para suas fotos. 2");
        novoProduto.setPreco(BigDecimal.valueOf(1005));
        novoProduto.setDataCriacao(LocalDateTime.now());
        novoProduto.setTags(List.of("Fotos"));
        
        entityManager.getTransaction().begin();
        novoProduto = entityManager.merge(novoProduto);
//        entityManager.flush();
        entityManager.getTransaction().commit();

//        entityManager.clear();
        
        Produto produtoVerificacao = entityManager.find(Produto.class, novoProduto.getId());
        assertNotNull(produtoVerificacao);
    }
    
    @Test
    public void mostrarDiferencaPersistMerge() {
        System.out.println("Persist");
        Produto produtoPersist = new Produto();
        
//        produtoPersist.setId(5);
        produtoPersist.setNome("Smartphone One Plus");
        produtoPersist.setDescricao("O processador mais rápido.");
        produtoPersist.setPreco(new BigDecimal(2000));
        produtoPersist.setDataCriacao(LocalDateTime.now());
        produtoPersist.setTags(List.of("Fotos"));
        
        entityManager.getTransaction().begin();
        entityManager.persist(produtoPersist);  //MANTÉM O MESMO OBJETO INSTANCIADO GERENCIADO
        produtoPersist.setNome("Smartphone Two Plus");
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificacaoPersist = entityManager.find(Produto.class, produtoPersist.getId());
        assertNotNull(produtoVerificacaoPersist);
        
        
        System.out.println("Merge");
        Produto produtoMerge = new Produto();
        
//        produtoMerge.setId(6);
        produtoMerge.setNome("Notebook Dell");
        produtoMerge.setDescricao("O melhor da categoria.");
        produtoMerge.setPreco(new BigDecimal(2000));
        
        entityManager.getTransaction().begin();
        produtoMerge = entityManager.merge(produtoMerge); //CRIA UM OUTRO OBJETO PARA SER GERENCIADO. OBJETO INSTANCIADO PELO USUÁRIO NÃO VAI FUNCIONAR
        produtoMerge.setNome("Notebook Dell 2");
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificacaoMerge = entityManager.find(Produto.class, produtoMerge.getId());
        assertNotNull(produtoVerificacaoMerge);
    }
    
    @Test
    public void atualizarObjeto() {
        Produto produto = new Produto();
        
//        produto.setId(1);
        produto.setNome("Kindle Paperwhite");
        produto.setDescricao("Conheça o novo Kindle.");
        produto.setPreco(new BigDecimal("5999"));
        produto.setDataCriacao(LocalDateTime.now());
        produto.setTags(List.of("Fotos"));
        
        entityManager.getTransaction().begin();
        produto = entityManager.merge(produto);
//        entityManager.flush();
        entityManager.getTransaction().commit(); //commit fecha a transação
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertNotNull(produtoVerificado);
        assertEquals("Kindle Paperwhite", produtoVerificado.getNome());
        assertEquals("Conheça o novo Kindle.", produtoVerificado.getDescricao());
        assertTrue(new BigDecimal("5999").compareTo(produto.getPreco()) == 0);
    }
    
    @Test
    public void atualizarObjetoGerenciado() {
        Produto produto = entityManager.find(Produto.class, 1);
        
        entityManager.getTransaction().begin();
        produto.setNome("Kindle Paperwhite 2° Geração");
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, 1);
        assertNotNull(produtoVerificado);
        assertEquals("Kindle Paperwhite 2° Geração", produtoVerificado.getNome());
    }
    
    @Test
    public void removerObjeto() {
//        Produto produto = new Produto();
//        produto.setId(1);
        
        Produto produto = entityManager.find(Produto.class, 1);
        
        entityManager.getTransaction().begin();
        entityManager.remove(produto);
        entityManager.getTransaction().commit();
        
        Produto produtoVerificado = entityManager.find(Produto.class, 1);
        assertNull(produtoVerificado);
    }
    
//    @Test
//    public void abrirEFecharATransacao() {
//        entityManager.getTransaction().begin();
//
//        Object o = new Object();
//        entityManager.persist(o);
//        entityManager.merge(o);
//        entityManager.remove(o);
//
//        entityManager.getTransaction().commit();
//    }
}