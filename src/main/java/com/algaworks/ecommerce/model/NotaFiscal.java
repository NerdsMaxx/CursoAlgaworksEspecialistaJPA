package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
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
    @NotEmpty
    @Lob
    @Column(length = 1000, nullable = false)
    private byte[] xml;
    
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_emissao", columnDefinition = "datetime(6) not null")
    private Date dataEmissao;
    
//    @Column(name = "pedido_id")
//    private Integer pedidoId;
    
    @NotNull
    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", insertable = false, updatable = false, nullable = false,
                foreignKey = @ForeignKey(name = "fk_nota_fiscal_pedido"))
//    @JoinTable(name = "pedido_nota_fiscal",
//               joinColumns = @JoinColumn(name = "nota_fiscal_id", unique = true),
//               inverseJoinColumns = @JoinColumn(name = "pedido_id", unique = true))
    private Pedido pedido;
    
    @PostLoad
    public void aoCarregar() {
        System.out.println("Após carregar a nota fiscal!");
    }
}