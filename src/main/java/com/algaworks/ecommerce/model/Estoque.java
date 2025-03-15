package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "estoque")
public class Estoque extends EntidadeBase {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

//    @Column(columnDefinition = "int(11) not null")
    @NotNull
    @PositiveOrZero
    @Column
    private Integer quantidade;
    
//    @Column(name = "produto_id")
//    private Integer produtoId;
    
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_estoque_produto"))
    private Produto produto;
}