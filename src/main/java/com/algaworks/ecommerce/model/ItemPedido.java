package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "item_pedido")
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "preco_produto")
    private BigDecimal precoProduto;
    
    private Integer quantidade;
    
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
//
//    @Column(name = "produto_id")
//    private Integer produtoId;
    
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
}