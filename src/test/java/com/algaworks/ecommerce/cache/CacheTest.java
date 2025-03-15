package com.algaworks.ecommerce.cache;

import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CacheTest {
    
    protected static EntityManagerFactory entityManagerFactory;
    
    @BeforeAll
    public static void setUpBeforeClass() {
        entityManagerFactory = Persistence
                .createEntityManagerFactory(
                        "Ecommerce-PU");
//                        , Map.of("jakarta.persistence.sharedCache.mode", "ALL"));
        out.println("OLHA AQUI " + entityManagerFactory.getProperties().get("jakarta.persistence.sharedCache.mode"));
    }
    
    @AfterAll
    public static void tearDownAfterClass() {
        entityManagerFactory.close();
    }
    
    @Test
    public void buscarDoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        //Ele não vai gerar a mesma consulta novamente, ele vai pegar
        //a mesma entidade que está no cache. É chamado cache de primeiro nível.
        //Cada EntityManager vai ter seu próprio cache, eles não são compartilhados
        //entre si. Por isso se fizer a mesma consulta com EntityManager diferente,
        //ele vai gerar a consulta novamente. Mas se usar
        //cache de segundo nível, ae pode funcionar o cache
        // entre entity manager diferentes.
        out.println("Buscando a partir da instância 1:");
        entityManager1.find(Pedido.class, 1);
        
        out.println("Buscando a partir da instância 2:");
        entityManager2.find(Pedido.class, 1);
        
        entityManager1.close();
        entityManager2.close();
    }
    
    @Test
    public void adicionarPedidosNoCache() {
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        
        out.println("Buscando a partir da instância 1:");
        //Ele não faz cache da consulta, ele faz cache das entidades.
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        out.println("Buscando a partir da instância 2:");
        //Ele só vai tirar proveito do cache quando pelo identificador específico
        entityManager2.find(Pedido.class, 1);
        
        out.println("Buscando de novo a partir da instância 2:");
        entityManager2.find(Pedido.class, 1);
        
//        entityManager2
//                .createQuery("select p from Pedido p", Pedido.class)
//                .getResultList();
        
        entityManager1.close();
        entityManager2.close();
    }
    
    @Test
    public void removerDoCachePedidoId1() {
        Cache cache = entityManagerFactory.getCache();
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        
        out.println("Buscando a partir da instância 1:");
        //Ele não faz cache da consulta, ele faz cache das entidades.
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        out.println("Removendo pedido 1 do cache");
        cache.evict(Pedido.class, 1);//Remove Pedido com ID 1
//        cache.evict(Pedido.class); Remove todas Pedidos do cache
//        cache.evictAll(); Remove todas entidades do cache
        
        out.println("Buscando a partir da instância 2:");
        //Aqui ele não vai estar mais no cache, então vai ter
        //q realizar consulta novamente
        entityManager2.find(Pedido.class, 1);
        
        out.println("Buscando novamente partir da instância 2:");
        //Já aqui, ele já busca do cache, pois só pedido de ID 1
        //não estava no cache, mas pedido de ID 2 está
        entityManager2.find(Pedido.class, 2);
        
//        entityManager2
//                .createQuery("select p from Pedido p", Pedido.class)
//                .getResultList();
        entityManager1.close();
        entityManager2.close();
    }
    
    @Test
    public void removerDoCacheTodosPedidos() {
        Cache cache = entityManagerFactory.getCache();
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        
        out.println("Buscando a partir da instância 1:");
        //Ele não faz cache da consulta, ele faz cache das entidades.
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        out.println("Removendo pedido 1 do cache");
//        cache.evict(Pedido.class, 1);//Remove Pedido com ID 1
        cache.evict(Pedido.class);// Remove todas Pedidos do cache
//        cache.evictAll();// Remove todas entidades do cache
        
        out.println("Buscando a partir da instância 2:");
        //Aqui ele não vai estar mais no cache, então vai ter
        //q realizar consulta novamente
        entityManager2.find(Pedido.class, 1);
        
        out.println("Buscando novamente partir da instância 2:");
        //Aqui ele não vai estar mais no cache, então vai ter
        //q realizar consulta novamente
        entityManager2.find(Pedido.class, 2);

//        entityManager2
//                .createQuery("select p from Pedido p", Pedido.class)
//                .getResultList();
        entityManager1.close();
        entityManager2.close();
    }
    
    @Test
    public void removerDoCacheTodasEntidades() {
        Cache cache = entityManagerFactory.getCache();
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        
        out.println("Buscando a partir da instância 1:");
        //Ele não faz cache da consulta, ele faz cache das entidades.
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        out.println("Removendo pedido 1 do cache");
//        cache.evict(Pedido.class, 1);//Remove Pedido com ID 1
//        cache.evict(Pedido.class);// Remove todas Pedidos do cache
        cache.evictAll();// Remove todas entidades do cache
        
        out.println("Buscando a partir da instância 2:");
        //Aqui ele não vai estar mais no cache, então vai ter
        //q realizar consulta novamente
        entityManager2.find(Pedido.class, 1);
        
        out.println("Buscando novamente a partir da instância 2:");
        //Aqui ele não vai estar mais no cache, então vai ter
        //q realizar consulta novamente
        entityManager2.find(Pedido.class, 2);

//        entityManager2
//                .createQuery("select p from Pedido p", Pedido.class)
//                .getResultList();
        entityManager1.close();
        entityManager2.close();
    }
    
    @Test
    public void verificarSeEstaNoCache() {
        Cache cache = entityManagerFactory.getCache();
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        assertTrue(cache.contains(Pedido.class, 1));
        entityManager1.close();
    }
    
    @Test
    public void controlarCacheDinamicamente1() {
        
        //jakarta.persistence.cache.retrieveMode - Quero usar cache de 2° nível ou não
        // jakarta.persistence.cache.storeMode - Quero colocar no cache de 2° nível ou não
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        
        out.println("Buscando a partir da instância 1:");
        //Ele não faz cache da consulta, ele faz cache das entidades.
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS) //não vai adicionar o resultado no cache
//                .setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.REFRESH) //vai atualizar resultado no cache de 2° nível
//                .setHint("jakarta.persistence.cache.storeMode", CacheStoreMode.USE) //vai adicionar resultado no cache de 2° nível, mas não vai atualizar
                .getResultList();
        
//        Map<String,Object> propriedades = Map.of("jakarta.persistence.cache.storeMode", CacheStoreMode.BYPASS);

//        Map<String,Object> propriedades = Map.of("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.USE); //Vai pegar resultado pelo cache de 2° nível
        out.println("Buscando novamente a partir da instância 2:");
        
        Map<String,Object> propriedades = Map.of("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS); //Não vai pegar resultado pelo cache, mesmo que o primeiro entity manager tenha guardado no cache
        
        entityManager2.find(Pedido.class, 2, propriedades);
        
        entityManager1.close();
        entityManager2.close();
    }
    
    @Test
    public void controlarCacheDinamicamente2() {
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        EntityManager entityManager3 = entityManagerFactory.createEntityManager();
        
        
        out.println("Buscando a partir da instância 1:");
        //Ele vai guardar o resultado no cache
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        
        out.println("Buscando novamente a partir da instância 2:");
        entityManager2.find(Pedido.class, 2);
        
        out.println("Buscando novamnete a partir da instância 3:");
        
        //OUTRA MANEIRA, MAS SE USAR setHint com createQuery, ae ele vai considerar do setHint, não do setProperty
//        entityManager3.setProperty("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        
        entityManager3
                .createQuery("select p from Pedido p", Pedido.class)
                .setHint("jakarta.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS) //NÃO VAI PEGAR RESULTADO NO CACHE DE 2° NÍVEL, ENTÃO ELE VAI REPETIR NA CONSULTA
                .getResultList();
        
        entityManager1.close();
        entityManager2.close();
        entityManager3.close();
    }
    
    @Test
    public void testandoUmaCoisaCuriosa() {
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        //Vai pegar pedido e colocar no cache de 2° nível
        out.println("Buscando a partir da instância 1:");
        entityManager1.find(Pedido.class, 1);
        
        //Apesar de ser Cliente, ele vai estar guardado no cache, já que cliente é EAGER na entidade Pedido. Mas se colocar como LAZY, cliente não vai estar guardado no cache, então entity manager 2 vai gerar a consulta de SQL.
        out.println("Buscando novamente a partir da instância 2:");
        entityManager2.find(Cliente.class, 1);
        
        entityManager1.close();
        entityManager2.close();
    }
    
    //DÊ UMA OLHADA NO src/main/resources/META-INF/ehcache.xml
    @Test
    public void ehcache() throws InterruptedException {
        Cache cache = entityManagerFactory.getCache();
        
        EntityManager entityManager1 = entityManagerFactory.createEntityManager();
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        
        out.println("Buscando e incluindo no cache...");
        entityManager1
                .createQuery("select p from Pedido p", Pedido.class)
                .getResultList();
        out.println("---");
        
        Thread.sleep(Duration.ofSeconds(1).toMillis());
        
        boolean contains1 = cache.contains(Pedido.class, 2);
        out.println("1 Contém no cache? " + contains1);
        assertTrue(contains1);
        
        entityManager2.find(Pedido.class, 2);
        
        Thread.sleep(Duration.ofSeconds(3).toMillis());
        
        boolean contains2 = cache.contains(Pedido.class, 2);
        out.println("2 Contém no cache? " + contains2);
        assertFalse(contains2);
        
        entityManager1.close();
        entityManager2.close();
    }
}