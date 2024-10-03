package com.algaworks.ecommerce.operacoesemcascata;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.ItemPedido;
import com.algaworks.ecommerce.model.ItemPedidoId;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CascadeTypeRemoveTest extends EntityManagerTest {
    
    @Test
    public void removerItensOrfaos() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        assertFalse(pedido.getItemPedidos().isEmpty());
        
        entityManager.getTransaction().begin();
        pedido.getItemPedidos().clear(); //não precisa chamar persist, pedido é gerenciado pela JPA (Hibernate)
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, pedido.getId());
        assertTrue(pedidoVerificacao.getItemPedidos().isEmpty());
    }
    
    @Test
    public void removerRelacaoProdutoCategoria() {
        Produto produto = entityManager.find(Produto.class, 1);
        
        assertFalse(produto.getCategorias().isEmpty());
        
        entityManager.getTransaction().begin();
        produto.getCategorias().clear();
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        assertTrue(produtoVerificado.getCategorias().isEmpty());
    }
    
    @Test
    public void removerPedidoEItens() {
        Pedido pedido = entityManager.find(Pedido.class, 1);
        
        entityManager.getTransaction().begin();
        entityManager.remove(pedido); // Necessário CascadeType.REMOVE no atributo "itens".
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, 1);
        assertNull(pedidoVerificado);
    }
    
    @Test
    public void removerItemPedidoEPedido() {
        ItemPedido itemPedido = entityManager.find(
                ItemPedido.class, new ItemPedidoId(1, 1));
        
        entityManager.getTransaction().begin();
        entityManager.remove(itemPedido); // Necessário CascadeType.REMOVE no atributo "pedido".
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificacao = entityManager.find(Pedido.class, itemPedido.getPedido().getId());
        assertNull(pedidoVerificacao);
    }
    
}