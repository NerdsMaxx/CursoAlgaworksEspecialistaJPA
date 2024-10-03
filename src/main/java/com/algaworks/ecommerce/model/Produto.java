package com.algaworks.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Length;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "produto",
       uniqueConstraints = {@UniqueConstraint(name = "unq_produto_nome", columnNames = "nome")},
       indexes = {@Index(name = "idx_produto_nome", columnList = "nome")})
public class Produto extends EntidadeBase {
    

//    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tabela")
//    @TableGenerator(name = "tabela",
//                    table = "hibernate_sequences",
//                    pkColumnName = "sequence_name",
//                    pkColumnValue = "produto",
//                    valueColumnName = "next_val")

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    
    @Column(name = "data_criacao", updatable = false, nullable = false)
    private LocalDateTime dataCriacao;
    
    @Column(name = "data_ultima_atualizacao", insertable = false)
    private LocalDateTime dataUltimaAtualizacao;
    
    //tem como colocar unique=true, mas ja tá configurado na @Table
    @Column(nullable = false, length = 100) // nome da coluna: "nome", tipo: varchar(255)
    private String nome;
    
//    @Column(columnDefinition = "varchar(275) default 'descricao'")
    @Lob //descricao tinytext
    @Column(length = Length.LONG32) // 2Gb - OBRIGANDO COLUNA TIPO DE DADOS COM MAIS BYTES, vai ser longtext agora.
    private String descricao;

//    @Column(precision = 10, scale = 2) // nome da coluna: "preco", tipo: decimal(10, 2)
    private BigDecimal preco;
    
    @Lob
    @Column(length = 1000)
    private byte[] foto;
    
//    @ManyToMany(cascade = CascadeType.PERSIST)
//    @ManyToMany(cascade = CascadeType.MERGE)
    @ManyToMany
    @JoinTable(name = "produto_categoria",
//               foreignKey = @ForeignKey(name = "fk_produto_produto_categoria"),
//               inverseForeignKey = @ForeignKey(name = "fk_categoria_produto_categoria"),
               joinColumns = @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_produto_categoria")),
               inverseJoinColumns = @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "fk_categoria_produto_categoria")))
    private List<Categoria> categorias;
    
//    @ToString.Exclude
//    @OneToMany(mappedBy = "produto")
//    private List<ItemPedido> itemPedidos;
    
    @OneToOne(mappedBy = "produto")
    private Estoque estoque;
    
    @ElementCollection
    @CollectionTable(
            name = "produto_tag",
            joinColumns = @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_produto_tag")))
    @Column(name = "tag", length = 50, nullable = false)
    private List<String> tags;
    
    @ElementCollection
    @CollectionTable(name = "produto_atributo",
                     joinColumns = @JoinColumn(name = "produto_id"), foreignKey = @ForeignKey(name = "fk_produto_atributo"))
    private List<Atributo> atributos;
}