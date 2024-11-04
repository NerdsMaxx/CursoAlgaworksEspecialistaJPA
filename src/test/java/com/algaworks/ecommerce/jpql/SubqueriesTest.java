package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SubqueriesTest extends EntityManagerTest {
    
    @Test
    public void pesquisarSubqueries() {
//        String jpql = "select p from Produto p " +
//                      "where p.preco = " +
//                      "(select p.preco from Produto p)";
//        Subquery deve retornar somente um resultado
        
        
        //O produto ou os produtos mais caros da base
        String jpql = "select p from Produto p " +
                      "where p.preco = " +
                      "(select max(p.preco) from Produto p)";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(p -> System.out.println(
                "Produto Id: " + p.getId() + " Preco: " + p.getPreco()));
        
        
        
        
//        Todos os pedidos acima da média de vendas
        jpql = "select p from Pedido p " +
               "where p.total > (select avg(p.total) " +
               "from Pedido p)";
        
        TypedQuery<Pedido> typedQuery1 = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> resultado1 = typedQuery1.getResultList();
        
        assertFalse(resultado1.isEmpty());
        
        resultado1.forEach(p ->  System.out.println(
                "Pedido Id: " + p.getId() + " Preco: " + p.getTotal()));
        
        
        
        
        jpql = "select c from Cliente c " +
               "where 100 < (select sum(p.total) " +
               "from Pedido p)";
        
        TypedQuery<Cliente> typedQuery2 = entityManager.createQuery(jpql, Cliente.class);
        
        List<Cliente> resultado2 = typedQuery2.getResultList();
        
        assertFalse(resultado2.isEmpty());
        
        resultado2.forEach(c ->  System.out.println(
                "Cliente Id: " + c.getId() + " Preco: " + c.getNome()));
       
    }
    
    @Test
    public void perquisarComSubqueryExercicio() {
        String jpql = "select c from Cliente c where " +
                      " (select count(cliente) from Pedido where cliente = c) >= 2";
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        
        List<Cliente> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
    
    @Test
    public void pesquisarComIN() {
        String jpql = "select ped from Pedido ped " +
                      "where ped.id in (select ped2.id from ItemPedido ip " +
                      "join ip.pedido ped2 " +
                      "join ip.produto pro2 " +
                      "where pro2.preco > 100)";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
    
    @Test
    public void pesquisarComINExercicio() {
        String jpql = "select p from Pedido p where p.id in " +
                      " (select p2.id from ItemPedido i2 " +
                      "      join i2.pedido p2 join i2.produto pro2 join pro2.categorias c2 where c2.id = 2)";
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
    
    @Test
    public void pesquisarComExists() {
        //Com exists, pode retornar qualquer resultado
        //where exists (select {qualquer coisa} from .....
        String jpql = "select pro from Produto pro " +
                      "where exists " +
                      "(select 1 from ItemPedido ip2 " +
                      "join ip2.produto pro2 " +
                      "where pro2 = pro)";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(obj -> System.out.println("ID: " + obj.getId()));
        
        
        
        jpql = "select pro from Produto pro " +
                      "where not exists " +
                      "(select 1 from ItemPedido ip2 " +
                      "join ip2.produto pro2 " +
                      "where pro2 = pro)";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        resultado = typedQuery.getResultList();
        assertTrue(resultado.isEmpty());
        
        resultado.forEach(obj -> System.out.println("ID: " + obj.getId()));
    }
    
    @Test
    public void pesquisarComAll() {
        String jpql = "select p from Produto p " +
                      "where p.preco  = ALL (select precoProduto " +
                      "from ItemPedido ip " +
                      "where ip.produto = p)";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(pro -> System.out.println("ID: " + pro.getId()));
        
        
        
        jpql = "select p from Produto p " +
               "where p.preco  > ALL (select precoProduto " +
               "from ItemPedido ip " +
               "where ip.produto = p)";
        
        //Outra alternativa para a mesma solução
//        jpql = "select p from Produto p " +
//               "where p.preco  > (select max(precoProduto) " +
//               "from ItemPedido ip " +
//               "where ip.produto = p)";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(pro -> System.out.println("ID: " + pro.getId()));
    }
    
    @Test
    public void pesquisarComAny() {
        String jpql = "select p from Produto p " +
                      "where p.preco  = ANY (" +
                      "select precoProduto from ItemPedido " +
                      "where produto = p)";
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        List<Produto> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(pro -> System.out.println("ID: " + pro.getId()));
        
        
        
//        jpql = "select p from Produto p " +
//               "where p.preco != ANY (" +
//               "select precoProduto from ItemPedido " +
//               "where produto = p)";

//        Pode usar SOME também no lugar do ANY
        jpql = "select p from Produto p " +
               "where p.preco != SOME (" +
               "select precoProduto from ItemPedido " +
               "where produto = p)";
        
        typedQuery = entityManager.createQuery(jpql, Produto.class);
        
        resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(pro -> System.out.println("ID: " + pro.getId()));
    }
}