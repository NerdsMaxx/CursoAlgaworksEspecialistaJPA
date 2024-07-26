package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "nota_fiscal")
public class NotaFiscal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String xml;
    
    @Column(name = "data_emissao")
    private Date dataEmissao;
    
    @Column(name = "pedido_id")
    private Integer pedidoId;
}