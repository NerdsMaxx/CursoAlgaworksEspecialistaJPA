package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.requireNonNull;

public class OperacoesEmLoteTest extends EntityManagerTest {
    
    private static final int LIMITE_INSERCOES = 4;
    
    @Test
    public void inserirEmLote() {
        final InputStream in = OperacoesEmLoteTest.class
                .getClassLoader().getResourceAsStream("produtos/importar.txt");
        
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        entityManager.getTransaction().begin();
        
        final AtomicInteger contador = new AtomicInteger(0);
        
        br.lines()
          .filter(l -> ! l.isBlank())
          .map(l -> l.split(";"))
          .forEach(l -> {
              Produto p = new Produto();
              
              p.setNome(l[0]);
              p.setDescricao(l[1]);
              p.setPreco(new BigDecimal(l[2]));
              p.setDataCriacao(LocalDateTime.now());
              
              System.out.println("nome = " + p.getNome());
              System.out.println("descricao = " + p.getDescricao());
              System.out.println("preco = " + p.getPreco());
              System.out.println("dataCriacao = " + p.getDataCriacao());
              
              entityManager.persist(p);
              
              if(contador.incrementAndGet() % LIMITE_INSERCOES == 0) {
                  entityManager.flush();
                  entityManager.clear();
                  
                  System.out.println("Contador " + (contador.get()/4) + " " + "-".repeat(15));
              }
          });
        
        entityManager.getTransaction().commit();
    }
    
    @Test
    public void atualizarEmLote() {
//        inserirEmLote();
        
        String jpql = "update Produto p set p.preco = p.preco + (p.preco * :multi) " +
                      "where id between 1 and 10";
        
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(jpql);
        query.setParameter("multi", new BigDecimal("0.1"));
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
    
    @Test
    public void removerEmLote() {
        String jpql = "delete from Produto p where p.id >= 5";
        
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery(jpql);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }
}