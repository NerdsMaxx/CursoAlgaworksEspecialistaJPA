package com.algaworks.ecommerce.concorrencia;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockPessimistaTest {
    
    protected static EntityManagerFactory entityManagerFactory;
    
    @BeforeAll
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence
                .createEntityManagerFactory("Ecommerce-PU");
    }
    
    @AfterAll
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }
    
    private static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException e) {}
    }
    
    @Test
    public void usarLockPessimistaLockModeTypePessimisticRead() {
        Runnable runnable1 = () -> {
            out.println("Iniciando Runnable 01.");
            
            String novaDescricao = "Descrição detalhada. CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.getTransaction().begin();
            
            out.println("Runnable 01 vai carregar o produto 1.");
            //LockModeType.PESSIMISTIC_READ permite que outras transações
            //leia registro, mas não permite comitar.
            Produto produto = entityManager1.find(
                    Produto.class, 1, LockModeType.PESSIMISTIC_READ);
            
            out.println("Runnable 01 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 01 vai esperar por 3 segundo(s).");
            esperar(3);
            
            out.println("Runnable 01 vai confirmar a transação.");
            entityManager1.getTransaction().commit();
            entityManager1.close();
            
            out.println("Encerrando Runnable 01.");
        };
        
        Runnable runnable2 = () -> {
            out.println("Iniciando Runnable 02.");
            
            String novaDescricao = "Descrição massa! CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            entityManager2.getTransaction().begin();
            
            out.println("Runnable 02 vai carregar o produto 2.");
            
            //Se dois runnables tiverem com LockModelType.PESSIMISTIC_READ
            //Quem comitar primeiro ganha!
            Produto produto = entityManager2.find(
//                    Produto.class, 1);
                    Produto.class, 1, LockModeType.PESSIMISTIC_READ);
            
            out.println("Runnable 02 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 02 vai esperar por 1 segundo(s).");
            esperar(1);
            
            out.println("Runnable 02 vai confirmar a transação.");
            entityManager2.getTransaction().commit();
            entityManager2.close();
            
            out.println("Encerrando Runnable 02.");
        };
        
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        thread1.start();
        
        esperar(1);
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
        
        //Se somente runnable 1 tiver LockModelType.PESSIMISTIC_READ
//        assertTrue(produto.getDescricao().startsWith("Descrição detalhada."));
        //Se dois runnables tiverem com LockModelType.PESSIMISTIC_READ
        assertTrue(produto.getDescricao().startsWith("Descrição massa!"));
        
        out.println("Encerrando método de teste.");
    }
    
    @Test
    public void usarLockPessimistaLockModeTypePessimisticWrite() {
        Runnable runnable1 = () -> {
            out.println("Iniciando Runnable 01.");
            
            String novaDescricao = "Descrição detalhada. CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.getTransaction().begin();
            
            out.println("Runnable 01 vai carregar o produto 1.");
            //LockModeType.PESSIMISTIC_WRITE vai bloquear as outros runnables (transações)
            //de comitar no banco. Ele não lança exceção como no modo READ.
            //Runnable 1 está com LockModeType.PESSIMISTIC_WRITE,
            //então ele vai comitar primeiro, mesmo que Runnable 2
            //tenha pedido commit primeiro. Depois q Runnable 1 comitar,
            //Runnable 2 pode comitar.
            //OBS.: LOCK OTIMISTA ATRAPALHA, ENTÃO TEM QUE TIRAR @Version das entidades, ao não ser que 2 Runnables usem LockModeType.PESSIMISTIC_WRITE
            //Se só um tive tiver usando este modo, então tem q tirar @Version
            Produto produto = entityManager1.find(
                    Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
            
            out.println("Runnable 01 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 01 vai esperar por 3 segundo(s).");
            esperar(3);
            
            out.println("Runnable 01 vai confirmar a transação.");
            entityManager1.getTransaction().commit();
            entityManager1.close();
            
            out.println("Encerrando Runnable 01.");
        };
        
        Runnable runnable2 = () -> {
            out.println("Iniciando Runnable 02.");
            
            String novaDescricao = "Descrição massa! CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            entityManager2.getTransaction().begin();
            
            //Se dois runnables tiverem LockModeType.PESSIMISTIC_WRITE, então quem executar primeiro, vai travar o commit do outro
            //Runnable 1 executou primeiro, então trava o commit do Runnable2
            //dps q Runnable 1 terminar, Runnable 2 pode comitar
            
            //Mas se Runnable 2 executasse antes do Runnable 1, ele ia travar
            //o commit do Runnable 1. Depois que Runnable 2 terminasse, que
            //ai Runnable 1 executa o commit.
            out.println("Runnable 02 vai carregar o produto 2.");
            Produto produto = entityManager2.find(
//                    Produto.class, 1);
            Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
            
            out.println("Runnable 02 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 02 vai esperar por 1 segundo(s).");
            esperar(1);
            
            out.println("Runnable 02 vai confirmar a transação.");
            entityManager2.getTransaction().commit();
            entityManager2.close();
            
            out.println("Encerrando Runnable 02.");
        };
        
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        
        thread1.start();
        esperar(1);
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
        
        //Se Runnable 1 executar primeiro.
        assertTrue(produto.getDescricao().startsWith("Descrição massa!"));
        //Se Runnable 2 executar primeiro.
//        assertTrue(produto.getDescricao().startsWith("Descrição detalhada."));
        
        out.println("Encerrando método de teste.");
    }
    
    @Test
    public void misturarTiposDeLocks() {
        Runnable runnable1 = () -> {
            out.println("Iniciando Runnable 01.");
            
            String novaDescricao = "Descrição detalhada. CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.getTransaction().begin();
            
            out.println("Runnable 01 vai carregar o produto 1.");
            Produto produto = entityManager1.find(
                    Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
            
            out.println("Runnable 01 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 01 vai esperar por 3 segundo(s).");
            esperar(3);
            
            out.println("Runnable 01 vai confirmar a transação.");
            entityManager1.getTransaction().commit();
            entityManager1.close();
            
            out.println("Encerrando Runnable 01.");
        };
        
        Runnable runnable2 = () -> {
            out.println("Iniciando Runnable 02.");
            
            String novaDescricao = "Descrição massa! CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            entityManager2.getTransaction().begin();
            
            out.println("Runnable 02 vai carregar o produto 2.");
            Produto produto = entityManager2.find(
                    Produto.class, 1, LockModeType.PESSIMISTIC_READ);
            
            out.println("Runnable 02 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 02 vai esperar por 1 segundo(s).");
            esperar(1);
            
            out.println("Runnable 02 vai confirmar a transação.");
            entityManager2.getTransaction().commit();
            entityManager2.close();
            
            out.println("Encerrando Runnable 02.");
        };
        
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        thread1.start();
        
        esperar(1);
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
        
        assertTrue(produto.getDescricao().startsWith("Descrição massa!"));
        
        out.println("Encerrando método de teste.");
    }
    
    @Test
    public void usarLockNaTypedQuery() {
        Runnable runnable1 = () -> {
            out.println("Iniciando Runnable 01.");
            
            String novaDescricao = "Descrição detalhada. CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager1 = entityManagerFactory.createEntityManager();
            entityManager1.getTransaction().begin();
            
            out.println("Runnable 01 vai carregar todos os produtos.");
            List<Produto> lista = entityManager1
                    .createQuery("select p from Produto p where p.id in (3,4,5)")
                    .setLockMode(LockModeType.PESSIMISTIC_READ)
                    .getResultList();
            
            Produto produto = lista
                    .stream()
                    .filter(p -> p.getId().equals(3))
                    .findFirst()
                    .get();
            
            out.println("Runnable 01 vai alterar o produto de ID igual a 1.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 01 vai esperar por 3 segundo(s).");
            esperar(3);
            
            out.println("Runnable 01 vai confirmar a transação.");
            entityManager1.getTransaction().commit();
            entityManager1.close();
            
            out.println("Encerrando Runnable 01.");
        };
        
        Runnable runnable2 = () -> {
            out.println("Iniciando Runnable 02.");
            
            String novaDescricao = "Descrição massa! CTM: " + System.currentTimeMillis();
            
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            entityManager2.getTransaction().begin();
            
            out.println("Runnable 02 vai carregar o produto 2.");
            Produto produto = entityManager2.find(
                    Produto.class, 1, LockModeType.PESSIMISTIC_WRITE);
            
            out.println("Runnable 02 vai alterar o produto.");
            produto.setDescricao(novaDescricao);
            
            out.println("Runnable 02 vai esperar por 1 segundo(s).");
            esperar(1);
            
            out.println("Runnable 02 vai confirmar a transação.");
            entityManager2.getTransaction().commit();
            entityManager2.close();
            
            out.println("Encerrando Runnable 02.");
        };
        
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        
        thread1.start();
        
        esperar(1);
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
        
        assertTrue(produto.getDescricao().startsWith("Descrição massa!"));
        
        out.println("Encerrando método de teste.");
    }
}