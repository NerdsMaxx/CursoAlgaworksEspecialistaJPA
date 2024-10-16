package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "item_pedido")
//@IdClass(ItemPedidoId.class)
public class ItemPedido {
    
    @Id
    @EmbeddedId
    private ItemPedidoId id;
   
    @Column(name = "preco_produto", precision = 19, scale = 2)
    private BigDecimal precoProduto;
    
    @Column(nullable = false)
    private Integer quantidade;
    
    @MapsId("pedidoId")
    @ManyToOne
//    @ManyToOne(optional = false, cascade = CascadeType.MERGE) //precisa de cascade para MERGE, diferente do PERSIST
//    @ManyToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "pedido_id", insertable = false, updatable = false)
    @JoinColumn(name = "pedido_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_item_pedido_pedido"))
    private Pedido pedido;
    
    @MapsId("produtoId")
    @ManyToOne(optional = false)
//    @JoinColumn(name = "produto_id", insertable = false, updatable = false)
    @JoinColumn(name = "produto_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_item_pedido_produto"))
    private Produto produto;
    
    
    //    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

//    @Id
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
//
//    @Id
//    @Column(name = "produto_id")
//    private Integer produtoId;
//
    
    //    @Column(name = "pedido_id")
//    private Integer pedidoId;
//
//    @Column(name = "produto_id")
//    private Integer produtoId;
    
    @PostLoad
    public void aoCarregar() {
        System.out.println("Após carregar o item pedido!");
    }
}