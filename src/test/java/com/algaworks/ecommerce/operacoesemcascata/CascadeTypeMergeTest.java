package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CascadeTypeMergeTest extends EntityManagerTest {
    
    @Test
    public void atualizarProdutoComCategoria() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setDataUltimaAtualizacao(LocalDateTime.now());
        produto.setPreco(new BigDecimal(500));
        produto.setNome("Kindle");
        produto.setDescricao("Agora com iluminação embutida ajustável.");
        
        Categoria categoria = new Categoria();
        categoria.setId(2);
        categoria.setNome("Tablets");
        
        produto.setCategorias(List.of(categoria)); // CascadeType.MERGE
        
        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Categoria categoriaVerificado = entityManager.find(Categoria.class, categoria.getId());
        assertEquals("Tablets", categoriaVerificado.getNome());
    }
    
    @Test
    public void atualizarPedidoComItens() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.AGUARDANDO);
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId(pedido, produto));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(3);
        itemPedido.setPrecoProduto(produto.getPreco());
        
        pedido.setItemPedidos(List.of(itemPedido));
        
        entityManager.getTransaction().begin();
        entityManager.merge(pedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        ItemPedido itemPedidoVerificado = entityManager.find(ItemPedido.class, itemPedido.getId());
        assertEquals(3, (int) itemPedidoVerificado.getQuantidade());
    }
    
    @Test
    public void atualizarItemPedidoComPedido() {
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.PAGO);
        
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(new ItemPedidoId(pedido, produto));
        itemPedido.setPedido(pedido); // CascadeType.MERGE
        itemPedido.setProduto(produto);
        itemPedido.setQuantidade(5);
        itemPedido.setPrecoProduto(produto.getPreco());
        
        pedido.setItemPedidos(List.of(itemPedido));
        
        entityManager.getTransaction().begin();
        entityManager.merge(itemPedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        ItemPedido itemPedidoVerificacao = entityManager.find(ItemPedido.class, itemPedido.getId());
        assertEquals(StatusPedido.PAGO, itemPedidoVerificacao.getPedido().getStatus());
    }
}