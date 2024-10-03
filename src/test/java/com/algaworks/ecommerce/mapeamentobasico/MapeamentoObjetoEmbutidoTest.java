package com.algaworks.ecommerce.mapeamentobasico;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.EnderecoEntregaPedido;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.StatusPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class MapeamentoObjetoEmbutidoTest extends EntityManagerTest {
    
    @Test
    public void analisarMapeamentoObjetoEmbutido() {
        EnderecoEntregaPedido endereco = new EnderecoEntregaPedido();
        endereco.setCep("00000-00");
        endereco.setLogradouro("Rua das Laranjeira");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Uberlândia");
        endereco.setEstado("MG");
        
        Pedido pedido = new Pedido();
//        pedido.setId(1);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO);
        pedido.setTotal(BigDecimal.valueOf(1000));
        pedido.setEnderecoEntrega(endereco);
        
        Cliente cliente = new Cliente();
        cliente.setId(1);
        
        pedido.setCliente(cliente);
        
        entityManager.getTransaction().begin();
        entityManager.persist(pedido);
        entityManager.getTransaction().commit();
        
        entityManager.clear();
        
        Pedido pedidoVerificado = entityManager.find(Pedido.class, pedido.getId());
        assertNotNull(pedidoVerificado);
        assertNotNull(pedidoVerificado.getEnderecoEntrega());
        assertNotNull(pedidoVerificado.getEnderecoEntrega().getCep());
    }
}