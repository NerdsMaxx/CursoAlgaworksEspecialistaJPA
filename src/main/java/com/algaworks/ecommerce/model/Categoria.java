package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "categoria")
public class Categoria {

//    @GeneratedValue(strategy = GenerationType.AUTO)

//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
//    @SequenceGenerator(name = "seq", sequenceName = "sequencias_chaves_primarias")
    
//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tabela")
//    @TableGenerator(name = "tabela",
//                    table = "hibernate_sequences",
//                    pkColumnName = "sequence_name",
//                    pkColumnValue = "categoria",
//                    valueColumnName = "next_val")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;
    
    @ManyToOne
    @JoinColumn(name = "categoria_pai_id")
    private Categoria categoriaPai;
    
    @OneToMany(mappedBy = "categoriaPai")
    private List<Categoria> categorias;
    
    @ManyToMany(mappedBy = "categorias")
    private List<Produto> produtos;
}