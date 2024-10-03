package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@DiscriminatorValue("cartao")
@Entity
//@Table(name = "pagamento_cartao")
public class PagamentoCartao extends Pagamento {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    
    @Column(name = "numero_cartao", length = 50, nullable = false)
    private String numeroCartao;
    
//    @Enumerated(EnumType.STRING)
//    private StatusPagamento status;
    
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
    
//    @OneToOne(optional = false)
//    @JoinColumn(name = "pedido_id")
//    private Pedido pedido;
}