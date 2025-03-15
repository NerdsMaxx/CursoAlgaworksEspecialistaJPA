package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CascadeTypePersistTest extends EntityManagerTest {
    
     @Test
    public void persistirProdutoComCategoria() {
        Produto produto = new Produto();
        produto.setDataCriacao(LocalDateTime.now());
        produto.setPreco(BigDecimal.TEN);
        produto.setNome("Fones de Ouvido");
        produto.setDescricao("A melhor qualidade de som");
        
        Categoria categoria = new Categoria();
        categoria.setNome("Áudio");
        
        produto.setCategorias(List.of(categoria)); // CascadeType.PERSIST
        
        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Categoria categoriaVerificado = entityManager.find(Categoria.class, categoria.getId());
        Assertions.assertNotNull(categoriaVerificado);
    }
    
     @Test
    public void persistirPedidoComItens() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setTotal(produto.getPreco());
        pedido.setStatus(StatusPedido.AGUARDANDO);
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(1);
        itemPedido.setPrecoProduto(produto.getPreco());
        
        pedido.setItemPedidos(List.of(itemPedido)); // CascadeType.PERSIST, faz sentido ter cascade no Pedido com ItemPedido
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificado);
        Assertions.assertFalse(pedidoVerificado.getItemPedidos().isEmpty());
    }
    
    @Test
    public void persistirItemPedidoComPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setTotal(produto.getPreco());
        pedido.setStatus(StatusPedido.AGUARDANDO);
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId());
        itemPedido.setPedido(pedido);// Não é necessário CascadeType.PERSIST porque possui @MapsId. Ele já é suficiente para salvar Pedido pelo ItemPedido
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(1);
        itemPedido.setPrecoProduto(produto.getPreco());
        
        entityManager.getTransaction().begin();
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        Assertions.assertNotNull(pedidoVerificado);
    }
    
     @Test
    public void persistirPedidoComCliente() {
        Cliente cliente = new Cliente();
        cliente.setDataNascimento(LocalDate.of(1980, 1, 1));
        cliente.setSexo(SexoCliente.MASCULINO);
        cliente.setNome("José Carlos");
        cliente.setCpf("01234567890");
        
        Pedido pedido = new Pedido();
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setCliente(cliente); // CascadeType.PERSIST só para ver funcionando, não é recomendado usar desta forma
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setStatus(StatusPedido.AGUARDANDO);
        
        entityManager.getTransaction().begin();
        entityManager.persist(cliente);// UMA SOLUÇÃO PARA NÃO USAR CascadeType.PERSIST
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Cliente clienteVerificado = entityManager.find(Cliente.class, cliente.getId());
        Assertions.assertNotNull(clienteVerificado);
    }
}