package com.algaworks.ecommerce.criteria;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressoesCondicionaisCriteiraTest extends EntityManagerTest {
    
    @Test
    public void usarExpressaoCondicionalLike() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.like(root.get(Cliente_.nome), "%a%"));
        
        TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Cliente> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }
    
    @Test
    public void usarIsNull() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        criteriaQuery.where(root.get(Produto_.foto).isNull());
//        criteriaQuery.where(criteriaBuilder.isNull(root.get(Produto_.foto)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(4, resultado.size());
    }
    
    @Test
    public void usarNotNull() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        criteriaQuery.where(root.get(Produto_.foto).isNotNull());
//        criteriaQuery.where(criteriaBuilder.isNotNull(root.get(Produto_.foto)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    public void usarIsEmpty() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.isEmpty(root.get(Produto_.categorias)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        
        resultado.forEach(r -> out.println("id = " + r.getId() + ", nome = " + r.getNome()));
    }
    
    
    @Test
    public void usarIsNotEmpty() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);
        root.fetch("categorias");

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        criteriaQuery.where(criteriaBuilder.isNotEmpty(root.get(Produto_.categorias)));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(4, resultado.size());
        
        resultado.forEach(r -> out.println(
                "id = " + r.getId() + ", nome = " + r.getNome() +
                ", categorias = " + r.getCategorias().stream()
                                     .map(Categoria::getNome)
                                     .toList()));
    }
    
    @Test
    public void usarMaior() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        //MetaModel obriga colocar o tipo certo do valor q é BigDecimal
        criteriaQuery.where(criteriaBuilder.greaterThan(root.get(Produto_.preco), new BigDecimal("2500.00")));
        
        //Tmb tem como usar maior ou igual.
//        criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get(Produto_.preco), new BigDecimal("2500.00")));
        
        //Sem MetaModel, você não precisa especificar o tipo, pode usar double normalmente
        //Mas com MetaModel é mais vantasojo
//        criteriaQuery.where(criteriaBuilder.greaterThan(root.get("preco"), 2500.00));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size()); //maior
//        assertEquals(3, resultado.size()); //maior ou igual
        
        resultado.forEach(
                r -> out.println("id = " + r.getId() + ", nome = " + r.getNome() + ", preco = " + r.getPreco()));
    }
    
    @Test
    public void usarMenor() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        //MetaModel obriga colocar o tipo certo do valor q é BigDecimal
        criteriaQuery.where(criteriaBuilder.lessThan(root.get(Produto_.preco), new BigDecimal("1500.00")));
        
        //Tmb tem como usar maior ou igual.
//        criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(root.get(Produto_.preco), new BigDecimal("1500.00")));
        
        //Sem MetaModel, você não precisa especificar o tipo, pode usar double normalmente
        //Mas com MetaModel é mais vantasojo
//        criteriaQuery.where(criteriaBuilder.lessThan(root.get("preco"), 1500.00));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size()); //menor
//        assertEquals(2, resultado.size()); //menor ou igual
        
        resultado.forEach(
                r -> out.println("id = " + r.getId() + ", nome = " + r.getNome() + ", preco = " + r.getPreco()));
    }
    
    @Test
    public void usarMaiorMenorJuntos() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
        Root<Produto> root = criteriaQuery.from(Produto.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        
        //MetaModel obriga colocar o tipo certo do valor q é BigDecimal
        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(root.get(Produto_.preco), new BigDecimal("1500.00")),
                criteriaBuilder.lessThan(root.get(Produto_.preco), new BigDecimal("3500.00")));
        
        TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Produto> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        
        resultado.forEach(
                r -> out.println("id = " + r.getId() + ", nome = " + r.getNome() + ", preco = " + r.getPreco()));
    }
    
    @Test
    public void usarMaiorMenorComDatas() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get(Pedido_.dataCriacao),
                        LocalDateTime.now().minusDays(3)));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> lista = typedQuery.getResultList();
        assertFalse(lista.isEmpty());
    }
    
    @Test
    public void usarBetween() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.between(
                root.get(Pedido_.total), new BigDecimal("499"), new BigDecimal("1000")));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        resultado.forEach(r -> out.println("id = " + r.getId() + ", total = " + r.getTotal()));
    }
    
    @Test
    public void usarExpressaoDiferente() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);

//        vai ser assim por padrão, não precisa ser explicito.
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.notEqual(
                root.get(Pedido_.total), new BigDecimal("499")));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        List<Pedido> resultado = typedQuery.getResultList();
        
        assertFalse(resultado.isEmpty());
        resultado.forEach(r -> out.println("id = " + r.getId() + ", total = " + r.getTotal()));
    }
    
    @Test
    public void usarExpressaoCase() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.multiselect(
                root.get(Pedido_.id), root.get(Pedido_.status),
                criteriaBuilder.selectCase(root.get(Pedido_.status))
                               .when(StatusPedido.PAGO, "Foi pago.")
                               .otherwise(root.get(Pedido_.status))
                               .as(String.class),
                criteriaBuilder.selectCase(root.get(Pedido_.pagamento).type())
                        .when(PagamentoBoleto.class, "Foi pago com boleto.")
                        .when(PagamentoCartao.class, "Foi pago com cartão")
                        .otherwise("Não identificado"));
        
        TypedQuery<Object[]> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Object[]> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r[0] +
                                           ", status = " + r[1] +
                                           ", case status = " + r[2] +
                                           ", case tipo pagamento = " + r[3]));
    }
    
    @Test
    public void usarExpressaoIN() {
        List<Integer> ids = List.of(1, 3, 4, 6);
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(root.get(Pedido_.id).in(ids));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId()));
    }
    
    @Test
    public void usarExpressaoINComClientesEntidades() {
        Cliente cliente1 = entityManager.find(Cliente.class, 1);
        Cliente cliente2 = new Cliente();
        cliente2.setId(2);
        
        List<Cliente> clientes = List.of(cliente1, cliente2);
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(root.get(Pedido_.cliente).in(clientes));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId()));
    }
    
    @Test
    public void usarExpressaoINComClientesIds() {
        Cliente cliente1 = entityManager.find(Cliente.class, 1);
        Cliente cliente2 = new Cliente();
        cliente2.setId(2);
        
        List<Integer> clientesIds = List.of(cliente1.getId(), cliente2.getId());
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteriaQuery = criteriaBuilder.createQuery(Pedido.class);
        Root<Pedido> root = criteriaQuery.from(Pedido.class);
        
        criteriaQuery.select(root);
        
        criteriaQuery.where(root.get(Pedido_.cliente)
                                .get(Cliente_.id)
                                .in(clientesIds));
        
        TypedQuery<Pedido> typedQuery = entityManager.createQuery(criteriaQuery);
        
        List<Pedido> resultado = typedQuery.getResultList();
        assertFalse(resultado.isEmpty());
        
        resultado.forEach(r -> out.println("id = " + r.getId()));
    }
}