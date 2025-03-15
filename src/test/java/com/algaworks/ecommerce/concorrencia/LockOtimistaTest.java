package com.algaworks.ecommerce.concorrencia;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockOtimistaTest extends EntityManagerTest {
    
    @Test
    public void usarLockOtimista() {
        //LEIA NESTA URL: https://blog.algaworks.com/entendendo-o-lock-otimista-do-jpa/
        //Usa uma coluna extra anotado com @Version para impedir que duas pessoas
        //alterem um dado ao mesmo tempo (não no mesmo instante), mas que ambos
        //pegaram a entidade numa versão 0 (inicial). Pessoa 2 altera, mas dps pessoa 1 altera.
        //vai dar certo para pessoa 2, mas vai dar erro para pessoa 1, já que quando
        //pessoa 2 alterou o dados, incrementou a versão para 1, mas a pessoa 1 ainda
        //está na versão 0, por isso dá erro.
        //(eu sei q texto está bosta, mas deu para entender)
        Runnable runnable1 = () -> {
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.getTransaction().begin();
            
            out.println("Runnable 01 vai carregar o produto 1.");
            Produto produto = entityManager1.find(Produto.class, 1);
            
            out.println("Runnable 01 vai esperar por 3 segundos.");
            esperar(3);
            
            out.println("Runnable 01 vai alterar o produto.");
            produto.setDescricao("Descrição detalhada.");
            
            out.println("Runnable 01 vai confirmar a transação.");
            entityManager1.getTransaction().commit();
            out.println("Runnable 01 confirmou a transação.");
            entityManager1.close();
        };
        
        Runnable runnable2 = () -> {
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            entityManager2.getTransaction().begin();
            
            out.println("Runnable 02 vai carregar o produto 1.");
            Produto produto = entityManager2.find(Produto.class, 1);
            
            out.println("Runnable 02 vai esperar por 1 segundo.");
            esperar(1);
            
            out.println("Runnable 02 vai alterar o produto.");
            produto.setDescricao("Descrição massa!");
            
            out.println("Runnable 02 vai confirmar a transação.");
            entityManager2.getTransaction().commit();
            out.println("Runnable 02 confirmou a transação.");
            entityManager2.close();
        };
        
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        Produto produto = entityManager3.find(Produto.class, 1);
        entityManager3.close();
        
        assertEquals("Descrição massa!", produto.getDescricao());
        
        out.println("Encerrando método de teste.");
    }
    
   
}