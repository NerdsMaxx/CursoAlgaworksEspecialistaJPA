package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "pagamento_boleto")
public class PagamentoBoleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "codigo_barras")
    private String codigoBarras;
    
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    
    @Column(name = "pedido_id")
    private Integer pedidoId;
}