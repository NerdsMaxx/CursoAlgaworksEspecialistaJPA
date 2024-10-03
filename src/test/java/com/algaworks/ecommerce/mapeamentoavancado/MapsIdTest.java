package com.algaworks.ecommerce.mapeamentoavancado;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapsIdTest extends EntityManagerTest {
    
    @Test
    public void inserirPagamento() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        NotaFiscal notaFiscal = new NotaFiscal();
//        notaFiscal.setId(pedido.getId()); com @MapsId não precisa
        notaFiscal.setPedido(pedido);
        notaFiscal.setDataEmissao(new Date());
//        notaFiscal.setXml("<xml/>");
        
        entityManager.getTransaction().begin();
        entityManager.persist(notaFiscal);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        NotaFiscal notaFiscalVerificado = entityManager.find(NotaFiscal.class, notaFiscal.getId());
        assertNotNull(notaFiscalVerificado);
        assertEquals(pedido.getId(), notaFiscalVerificado.getId());
    }
    
    @Test
    public void inserirItemPedido() {
        
        Cliente cliente = entityManager.find(Cliente.class, 1);
        Produto produto = entityManager.find(Produto.class, 1);
        
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setTotal(produto.getPreco());
        
        ItemPedido itemPedido = new ItemPedido();
//        itemPedido.setPedidoId(pedido.getId()); IdClass
//        itemPedido.setProdutoId(produto.getId()); IdClass
        itemPedido.setId(new ItemPedidoId());
//        itemPedido.setId(new ItemPedidoId(pedido.getId(), produto.getId()));
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setPrecoProduto(produto.getPreco());
        itemPedido.setQuantidade(1);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.persist(itemPedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        ItemPedido itemPedidoVerificado = entityManager.find(ItemPedido.class, new ItemPedidoId(pedido.getId(), produto.getId()));
        Assertions.assertNotNull(itemPedidoVerificado);
    }
}