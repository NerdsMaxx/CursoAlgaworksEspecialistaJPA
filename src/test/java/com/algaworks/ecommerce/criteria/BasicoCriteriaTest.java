package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDto;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Cliente_;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BasicoCriteriaTest extends EntityManagerTest {
    
    @Test
    public void buscarPorIdentificador() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        Pedido pedido = typedQuery.getResultStream()
                          .findFirst()
                          .orElse(null);
        
        assertNotNull(pedido);
    }
    
    @Test
    public void selecionarUmAtributoParaRetorno() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root.get("cliente"));
        
        criteriaQuery.where(criteriaBuilder.equal(root.get("id"), 1));
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        
        Cliente cliente = typedQuery.getResultStream()
                                  .findFirst()
                                  .orElse(null);
        
        assertNotNull(cliente);
        assertEquals("Guilherme Henrique", cliente.getNome());
        
        
        

        
        CriteriaBuilder criteriaBuilder1 = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery1 = criteriaBuilder1.createQuery(BigDecimal.class);
        Root<Pedido> root1 = criteriaQuery1.from(Pedido.class);
        
        criteriaQuery1.select(root1.get("total"));
        
        criteriaQuery1.where(criteriaBuilder.equal(root1.get("id"), 1));
        
        TypedQuery<BigDecimal> typedQuery1 = entityManager.createQuery(criteriaQuery1);
        BigDecimal total = typedQuery1.getSingleResult();
        
        assertEquals(new BigDecimal("998.00"), total);
    }
    
    @Test
    public void retornarTodosOsProdutosExercicio() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
//        assertEquals(4, resultado.size());
    }
    
    @Test
    public void projetarOResultado() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

        criteriaQuery.multiselect(root.get("id"), root.get("nome"));
        
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Object[]> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
//        assertEquals(4, resultado.size());
        
        resultado.forEach(r -> System.out.println("id = " + r[0] + ", nome = " + r[1]));
    }
    
    @Test
    public void projetarOResultadoTuple() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        criteriaQuery.multiselect(root.get("id"), root.get("nome"));
        criteriaQuery.select(
                criteriaBuilder.tuple(root.get("id").alias("id"),
                                      root.get("nome").alias("nome")));
        
        
        TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Tuple> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
//        assertEquals(4, resultado.size());
        
        resultado.forEach(r -> System.out.println("id = " + r.get(0) + ", nome = " + r.get(1)));
        
        
        //Outro jeito, com alias
        System.out.println("\nOutro jeito, com alias");
        resultado.forEach(r -> System.out.println("id = " + r.get("id") + ", nome = " + r.get("nome")));
    }
    
    @Test
    public void projetarOResultadoDTO() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProdutoDto> criteriaQuery = criteriaBuilder.createQuery(ProdutoDto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        
        criteriaQuery.select(criteriaBuilder.construct(ProdutoDto.class, root.get("id"), root.get("nome")));
        
        
        TypedQuery<ProdutoDto> typedQuery = entityManager.createQuery(criteriaQuery);
        List<ProdutoDto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
//        assertEquals(4, resultado.size());
        
        resultado.forEach(r -> System.out.println("id = " + r.getId() + ", nome = " + r.getNome()));
    }
    
    @Test
    public void ordernarResultados() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);
        
        criteriaQuery.select(root);
        
        //no método orderBy tu coloca quantas tu quiser, já que é um vararg
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(Cliente_.nome)),
                              criteriaBuilder.asc(root.get(Cliente_.cpf)));
        
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> System.out.println("id = " + r.getId() + ", nome = " + r.getNome() + ", cpf = " + r.getCpf()));
    }
}