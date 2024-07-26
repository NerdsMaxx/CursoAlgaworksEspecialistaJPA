package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "produto")
public class Produto {
    

//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tabela")
//    @TableGenerator(name = "tabela",
//                    table = "hibernate_sequences",
//                    pkColumnName = "sequence_name",
//                    pkColumnValue = "produto",
//                    valueColumnName = "next_val")

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;
    
    private String descricao;
    
    private BigDecimal preco;
    
    @ManyToMany
    @JoinTable(name = "produto_categoria",
               joinColumns = @JoinColumn(name = "produto_id"),
               inverseJoinColumns = @JoinColumn(name = "categoria_id"))
    private List<Categoria> categorias;
    
//    @ToString.Exclude
//    @OneToMany(mappedBy = "produto")
//    private List<ItemPedido> itemPedidos;
}