package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@DiscriminatorValue("boleto")
@Entity
//@Table(name = "pagamento_boleto")
public class PagamentoBoleto extends Pagamento {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    
    @Column(name = "codigo_barras", length = 100)
    private String codigoBarras;
    
//    @Enumerated(EnumType.STRING)
//    private StatusPagamento status;
    
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
    
//    @OneToOne(optional = false)
//    @JoinColumn(name = "pedido_id")
//    private Pedido pedido;
}