package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "categoria",
       uniqueConstraints = {@UniqueConstraint(name = "unq_categoria_nome", columnNames = "nome")},
       indexes = {@Index(name = "idx_categoria_nome", columnList = "nome")})
public class Categoria extends EntidadeBase {

//    @GeneratedValue(strategy = GenerationType.AUTO)

//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
//    @SequenceGenerator(name = "seq", sequenceName = "sequencias_chaves_primarias")
    
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tabela")
//    @TableGenerator(name = "tabela",
//                    table = "hibernate_sequences",
//                    pkColumnName = "sequence_name",
//                    pkColumnValue = "categoria",
//                    valueColumnName = "next_val")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    
    @Column(length = 100, nullable = false)
    private String nome;
    
    @ManyToOne
    @JoinColumn(name = "categoria_pai_id",
                foreignKey = @ForeignKey(name = "fk_categoria_categoria"))
    private Categoria categoriaPai;
    
    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> categorias;
    
    @ManyToMany(mappedBy = "categorias")
    private List<Produto> produtos;
}