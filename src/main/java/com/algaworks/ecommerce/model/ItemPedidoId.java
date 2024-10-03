package com.algaworks.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"pedidoId", "produtoId"})
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ItemPedidoId implements Serializable {
    
    @Column(name = "pedido_id")
    private Integer pedidoId;
    
    @Column(name = "produto_id")
    private Integer produtoId;
    
    public ItemPedidoId(Pedido pedido, Produto produto) {
        pedidoId = pedido.getId();
        produtoId = produto.getId();
    }
}