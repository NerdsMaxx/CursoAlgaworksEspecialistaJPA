package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDto;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


public class BasicoJPQLTest extends EntityManagerTest {
    
    @Test
    public void buscarPorIdentificador() {
        // Java Persistence Query Language - JPQL
        
        // JPQL - select p
        // from Pedido p
        // join p.itemPedidos ip
        // where ip.precoProduto > 10
        
        // SQL Nativa - select p.*
        // from pedido
        // join item_pedido ip on p.id = ip.pedido_id
        // where ip.preco_produto > 10
        
        TypedQuery<Pedido> typedQuery = entityManager //Vai trazer tanto dados do pedido quanto dados das outras entidades que está relacionado com Pedido que esteja marcado com FetchType.EAGER
                                                      .createQuery("select p from Pedido p where p.id = 1",
                                                                   Pedido.class);

//        Pedido pedido = typedQuery.getSingleResult(); //Pode dar erro se tiver 0 ou mais de 1 resultados
//        assertNotNull(pedido);
        
        Optional<Pedido> pedido = typedQuery.getResultStream().findFirst();
        assertTrue(pedido.isPresent());
    }
    
    @Test
    public void mostrarDiferencaQueries() {
        String jpql = "select p from Pedido p where p.id = 1";
        
        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery(jpql, Pedido.class);
        Optional<Pedido> pedido1 = typedQuery.getResultStream().findFirst();
        assertTrue(pedido1.isPresent());

//        TypedQuery<Pedido> typedQuery = entityManager
//                .createQuery(jpql, Pedido.class);
//        Pedido pedido1 = typedQuery.getSingleResult();
//        assertNotNull(pedido1);
        
        Query query = entityManager.createQuery(jpql);
        Optional<Pedido> pedido2 = (Optional<Pedido>) query.getResultStream()
                                                           .map(Pedido.class::cast)
                                                           .findFirst();
        assertTrue(pedido2.isPresent());

//        Query query = entityManager.createQuery(jpql);
//        Pedido pedido2 = (Pedido) query.getSingleResult();
//        assertNotNull(pedido2);
        
        Query query1 = entityManager.createQuery(jpql);
        List<Pedido> pedidoList = query1.getResultList();
        
        assertFalse(pedidoList.isEmpty());
    }
    
    @Test
    public void selecionarUmAtributoParaRetorno() {
        String jpql = "select p.nome from Produto p";
        
        TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
        
        List<String> nomesDosProdutos = typedQuery.getResultList();
        
//        nomesDosProdutos.forEach(System.out::println);
        IntStream.range(0, nomesDosProdutos.size())
                 .mapToObj(i -> String.format("%d) %s", i + 1, nomesDosProdutos.get(i)))
                 .forEach(System.out::println);
        
        assertFalse(nomesDosProdutos.isEmpty());
        assertTrue(nomesDosProdutos.get(0).getClass().equals(String.class));
        
        
        String jpqlCliente = "select p.cliente from Pedido p";
        TypedQuery<Cliente> typedQueryCliente = entityManager.createQuery(jpqlCliente, Cliente.class);
        List<Cliente> clientes = typedQueryCliente.getResultList();
        
        assertFalse(clientes.isEmpty());
        assertTrue(clientes.get(0).getClass().equals(Cliente.class));
    }
    
    @Test
    public void projetarOResultado() {
        String jpql = "select p.id, p.nome from Produto p";
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> resultadoLista = typedQuery.getResultList();

        assertFalse(resultadoLista.isEmpty());
        assertEquals(2, resultadoLista.get(0).length);

        resultadoLista.forEach(arr -> System.out.println(Arrays.toString(arr)));
        
        TypedQuery<Tuple> typedQuery1 = entityManager.createQuery(jpql, Tuple.class);
        List<Tuple> resultadoLista1 = typedQuery1.getResultList();
        
        assertFalse(resultadoLista1.isEmpty());
        
//        resultadoLista1.forEach(t -> System.out.println("[id=" + t.get("id") + ", nome=" + t.get("nome") + "]"));
        resultadoLista1.forEach(System.out::println);
        
        
        //NÃO VAI FUNCIONAR!!!! TEM Q TER SPRING DATA JPA PARA FUNCIONAR.
//        interface ProdutoProj {
//            Long getId();
//            String getNome();
//        }
//
//        TypedQuery<ProdutoProj> typedQuery1 = entityManager.createQuery(jpql, ProdutoProj.class);
//        List<ProdutoProj> resultadoLista1 = typedQuery1.getResultList();
//
//        assertFalse(resultadoLista1.isEmpty());
//
//        resultadoLista1.forEach(proj -> System.out.println("[id=" + proj.getId() + ", nome=" + proj.getNome() + "]"));
    }
    
    @Test
    public void projetarNoDTO() {
        String jpql = "select new com.algaworks.ecommerce.dto.ProdutoDto(p.id, p.nome) from Produto p";
        
        TypedQuery<ProdutoDto> typedQuery = entityManager.createQuery(jpql, ProdutoDto.class);
        List<ProdutoDto> produtosDtos = typedQuery.getResultList();
        
        assertFalse(produtosDtos.isEmpty());
        
        produtosDtos.forEach(p -> System.out.println("[id=" + p.getId() + ", nome=" + p.getNome() + "]"));
    }
    
    @Test
    public void ordernarResultados() {
        String jpql = "select c from Cliente c order by c.nome desc";
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);
        
        List<Cliente> clientes = typedQuery.getResultList();
        
        clientes.forEach(System.out::println);
        
        assertFalse(clientes.isEmpty());
    }
}