package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
//    @Column(name = "cliente_id")
//    private Integer clienteId;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    
    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;
    
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @Column(name = "nota_fiscal_id")
    private Integer notaFiscalId;
    
    @Embedded
    private EnderecoEntregaPedido enderecoEntrega;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itemPedidos;
}