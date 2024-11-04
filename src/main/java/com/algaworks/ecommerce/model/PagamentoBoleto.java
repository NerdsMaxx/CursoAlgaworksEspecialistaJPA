package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    
    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
    
//    @Enumerated(EnumType.STRING)
//    private StatusPagamento status;
    
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
    
//    @OneToOne(optional = false)
//    @JoinColumn(name = "pedido_id")
//    private Pedido pedido;
}