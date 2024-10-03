package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "nota_fiscal")
public class NotaFiscal extends EntidadeBase {
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    
//    @Id
//    @Column(name = "pedido_id")
//    private Integer id;
    
//    private String xml;
    @Lob
    @Column(length = 1000, nullable = false)
    private byte[] xml;
    
    @Column(name = "data_emissao", columnDefinition = "datetime(6) not null")
    private Date dataEmissao;
    
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
    
    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", insertable = false, updatable = false, nullable = false,
                foreignKey = @ForeignKey(name = "fk_nota_fiscal_pedido"))
//    @JoinTable(name = "pedido_nota_fiscal",
//               joinColumns = @JoinColumn(name = "nota_fiscal_id", unique = true),
//               inverseJoinColumns = @JoinColumn(name = "pedido_id", unique = true))
    private Pedido pedido;
}